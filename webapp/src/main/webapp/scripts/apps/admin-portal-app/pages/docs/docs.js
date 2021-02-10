'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('docs', {
            parent: 'admin',
            url: '/docs',
            data: {
                pageTitle: 'Rest APIs',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    templateUrl: 'scripts/apps/admin-portal-app/pages/docs/docs.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate) {
                    return $translate.refresh();
                }
            }
        });
    });
})();
