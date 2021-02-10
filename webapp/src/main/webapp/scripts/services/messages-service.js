'use strict';

(function() {
    var module = angular.module('app.services');

    module.service('MessagingService',
        function($http, $q) {
            var getMessages = function(body) {
                    return $http({
                        url: 'api/message/retrieve',
                        method: 'POST',
                        data: body
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },

                dismissMessage = function(params) {
                    return $http({
                        url: 'api/message/dismiss/' + params.id,
                        method: 'POST'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                dismissAllMessages = function(successCallback) {
                    return $http({
                        url: 'api/message/dismissAll',
                        method: 'POST'
                    }).then(function success(response) {
                        return successCallback(response);
                    }, function error(response) {
                        return $q.reject(response);
                    });
                },
                pullMessages = function(params) {
                    return $http({
                        url: 'api/message/pull',
                        method: 'GET'
                    }).then(function success(response) {
                        return response.data;
                    }, function error(response) {
                        return $q.reject(response);
                    });
                };

            return {
                getMessages: getMessages,
                dismissMessage: dismissMessage,
                dismissAllMessages: dismissAllMessages,
                pullMessages: pullMessages
            };
        }
    );
})();
