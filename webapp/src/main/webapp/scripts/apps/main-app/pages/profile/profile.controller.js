'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('ProfileController',
        function($rootScope, $scope, ProfileService, FileUploader) {

            var init = function() {
                $scope.uploader = makeUploader();
                $scope.loading = true;
                ProfileService.getProfile(function(response) {
                    $scope.loading = false;
                    $scope.profile = response;
                    $scope.profileImageUrl = $scope.profile.pictureUrl + "?" + new Date().getTime();
                }, function(error) {
                    console.log(error);
                });

                $scope.$on('onUpdatedImage', function(event, data) {
                    $scope.profileImageUrl = data;
                });

            };

            var makeUploader = function() {
                var uploader = new FileUploader({
                    onAfterAddingFile: function(item) {
                        $scope.fileName = item.file.name;
                        $scope.readyToUpload = true;
                        uploader.uploadAll();
                    },
                    onBeforeUploadItem: function(item) {
                        $scope.uploadInProgress = true;
                    },
                    onCompleteItem: function(item, response, status, headers) {
                        $scope.uploadInProgress = false;
                        $scope.list = response;
                        $scope.display = true;
                        $scope.profileImageUrl = $scope.profile.pictureUrl + "?" + new Date().getTime();
                        $rootScope.$broadcast('onUpdatedImage', $scope.profileImageUrl);
                    },
                    onErrorItem: function(item, response, status, headers) {
                        var jsonResponse = angular.fromJson(response);
                        alert(jsonResponse.message);
                        $scope.list = null;
                        $scope.uploadInProgress = false;
                        $scope.error = true;
                    },
                    url: 'api/profile/image'
                });
                return uploader;
            };

            init();

            return {};
        }
    );

    module.factory('ProfileService', function($resource) {
        return $resource('api/profile/', {}, {
            getProfile: {
                method: 'GET'
            }
        });
    });
})();
