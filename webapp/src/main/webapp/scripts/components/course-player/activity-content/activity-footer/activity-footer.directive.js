'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('activityFooter',
        function() {
            return {
                bindToController: {
                    nextActivity: '=',
                    onNextActivityClick: '&onNextActivityClick'
                },
                controller: 'ActivityFooterController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/course-player/activity-content/activity-footer/activity-footer.html'
            }
        }
    );

    module.controller('ActivityFooterController',
        function() {
            var data = {
                    nextActivity: this.nextActivity
                },

                onNextActivityClick = this.onNextActivityClick,

                goToNextActivity = function() {
                    if (onNextActivityClick && !isNextActivityLocked()) {
                        onNextActivityClick();
                    }
                },

                isNextActivityLocked = function() {
                    return !!data.nextActivity.activityStatus.isLocked;
                },

                zend;

            return {
                data: data,
                goToNextActivity: goToNextActivity,
                isNextActivityLocked: isNextActivityLocked
            }
        }
    );
})();
