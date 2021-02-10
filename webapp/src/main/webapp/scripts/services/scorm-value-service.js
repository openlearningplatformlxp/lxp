'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('ScormValueService',
        function($rootScope, $http, $q) {

            var setScormValue = function(params) {
                return $http({
                    url: 'api/scorm/set/' + params.activityId,
                    method: 'POST',
                    data: {
                        key: params.key,
                        value: params.value,
                        allowCompletion: params.allowCompletion
                    }
                }).then(function success(response) {
                    if (response.data.isCompleted && response.data.activityStatuses) {
                        $rootScope.$broadcast("course-player-service.activityStatusesUpdated", {
                            activityStatuses: response.data.activityStatuses
                        });
                    }
                    return response.data;
                }, function error(response) {
                    return $q.reject(response);
                });
            };

            return {
                setScormValue: setScormValue
            };
        }
    );
})();
