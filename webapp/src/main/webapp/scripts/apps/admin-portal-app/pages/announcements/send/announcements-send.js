'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('announcements-send', {
            parent: 'admin',
            url: '/announcements/send',
            data: {
                pageTitle: 'Send Notification',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminAnnouncementsSendController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/announcements/send/announcements-send.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('announcements');
                    return $translate.refresh();
                }
            }
        });
    });
})();
