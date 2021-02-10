'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('ProfileMainController',
        function(ProfileProgressInfoService) {

        var data = {};

            var init = function() {
                data.loading = true;
                ProfileProgressInfoService.getProfileProgress(function(response) {
                    data.loading = false;
                    data.profileProgress = response;
                }, function(error) {
                    console.log(error);
                });
            };

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };

            init();

            return {
                data: data,
                notYetImplemented: notYetImplemented
            };
        }
    );

    module.factory('ProfileProgressInfoService', function($resource) {
        return $resource('api/profile/progress', {}, {
            getProfileProgress: {
                method: 'GET'
            }
        });
    });
})();
