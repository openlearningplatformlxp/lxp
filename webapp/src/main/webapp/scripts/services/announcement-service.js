'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('AnnouncementService',
        function($http, $q) {
            var pullAnnouncement = function() {
                    return $http({
                        url: 'api/announcement/',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                dismissAnnouncement = function(announcementId) {
                    return $http({
                        url: 'api/announcement/dismiss/' + announcementId + "/",
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                pullAnnouncement: pullAnnouncement,
                dismissAnnouncement: dismissAnnouncement
            };
        }
    );
})();
