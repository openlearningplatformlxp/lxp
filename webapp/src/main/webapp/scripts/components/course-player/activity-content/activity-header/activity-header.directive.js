'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('activityHeader',
        function() {
            return {
                bindToController: {
                    activityType: '<',
                    activityTypeDisplayName: '<',
                    activityName: '<'
                },
                controller: 'ActivityHeaderController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/course-player/activity-content/activity-header/activity-header.html'
            }
        }
    );

    module.controller('ActivityHeaderController',
        function() {
            var data = {
                    name: this.activityName,
                    typeDisplayName: this.activityTypeDisplayName,
                },

                activityType = this.activityType,

                init = function() {
                    data.typeIconClass = "fa-list";

                    if (activityType === 'VIDEO') {
                        data.typeIconClass = "fa-play-circle";
                    } else if (activityType === 'URL') {
                        data.typeIconClass = "fa-link";
                    } else if (activityType === 'SCORM') {
                        data.typeIconClass = "fa-question-circle";
                    } else if (activityType === 'QUIZ') {
                        data.typeIconClass = "fa-question-circle";
                    }
                },

                zend;

            init();

            return {
                data: data
            }
        }
    );
})();
