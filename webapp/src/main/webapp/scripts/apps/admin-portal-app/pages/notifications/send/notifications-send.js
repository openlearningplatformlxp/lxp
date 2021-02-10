'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('notifications-send', {
            parent: 'admin',
            url: '/notifications/send',
            data: {
                pageTitle: 'Send Notification',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminNotificationsSendController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/notifications/send/notifications-send.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/admin/programs/').then(
                        function success(response) {
                            return {
                                programs: response.data,
                            };
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notifications');
                    return $translate.refresh();
                }
            }
        });
    });
})();
