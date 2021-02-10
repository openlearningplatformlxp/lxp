'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('announcement',
        function() {

            return {
                restrict: 'E',
                templateUrl: 'scripts/components/directives/announcement/announcement.html',
                link: function(scope, element, attrs) {},
                controller: function($rootScope, $scope, AnnouncementService) {

                    $scope.init = function() {
                        AnnouncementService.pullAnnouncement().then(function(announcement) {
                            $scope.announcement = announcement;
                            $rootScope.$broadcast("announcement", announcement);
                        });
                    };

                    $scope.dismiss = function(id) {
                        AnnouncementService.dismissAnnouncement(id).then(function(announcement) {
                            $scope.announcement = announcement;
                            $rootScope.$broadcast("announcement", announcement);
                        });
                    }

                    $scope.notYetImplemented = function() {
                        window.alert('Not yet implemented');
                    };

                    $scope.init();
                }
            };
        }
    );
    module.directive('announcementSpace',
        function() {

            return {
                restrict: 'E',
                template: '<div class="announcement-space" ng-if="announcement"></div>',
                link: function(scope, element, attrs) {},
                controller: function($rootScope, $scope, AnnouncementService) {

                    $rootScope.$on("announcement", function(event, announcement) {
                        $scope.announcement = announcement;
                    });
                }
            };
        }
    );
})();
