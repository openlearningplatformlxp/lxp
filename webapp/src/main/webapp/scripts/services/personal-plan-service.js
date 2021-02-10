'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('PersonalPlanService',
        function($http, $q) {
            var saveOrUpdatePersonalPlan = function(params) {
                    var method = params.id ? 'PUT' : 'POST';

                    return $http({
                        url: 'api/course/program/personal/',
                        method: method,
                        data: params
                    }).then(function success(response) {
                        return response;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                clonePersonalPlan = function(id) {
                    return $http({
                        url: 'api/course/program/personal/clone/' + id,
                        method: 'POST'
                    }).then(function success(response) {
                        return response;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                archivePlan = function(params) {
                    return $http({
                        url: 'api/course/program/personal/' + params.id + '/archive',
                        method: 'PUT',
                        data: params
                    }).then(function success(response) {
                        return response;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                shareWithDirectReports = function(params) {
                    return $http({
                        url: 'api/course/program/personal/share/direct-reports/',
                        method: 'POST',
                        data: params
                    }).then(function success(response) {
                        return response;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                shareWithManager = function(params) {
                    return $http({
                        url: 'api/course/program/personal/share/manager/',
                        method: 'POST',
                        data: params
                    }).then(function success(response) {
                        return response;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                saveOrUpdatePersonalPlan: saveOrUpdatePersonalPlan,
                clonePersonalPlan: clonePersonalPlan,
                archivePlan: archivePlan,
                shareWithManager: shareWithManager,
                shareWithDirectReports: shareWithDirectReports
            };
        }
    );
})();
