'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('TeamService',
        function($http, $q) {
            var getTeamData = function(type) {
                    var params = "?";
                    if (type != null) {
                        params += "type=" + type;
                    }
                    return $http({
                        url: 'api/team' + params,
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                getTeamPLPData = function() {
                    return $http({
                        url: 'api/team/personal',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                getTeamChildData = function(memberId, type) {
                    var type = "";
                    if (type !== '') {
                        type += "/" + type;
                    }
                    return $http({
                        url: 'api/team/child/' + memberId + type,
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                getTeamMemberProgress = function(memberId) {
                    return $http({
                        url: 'api/team/' + memberId + "/progress",
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                getTeamData: getTeamData,
                getTeamChildData: getTeamChildData,
                getTeamMemberProgress: getTeamMemberProgress,
                getTeamPLPData: getTeamPLPData
            };
        }
    );
})();
