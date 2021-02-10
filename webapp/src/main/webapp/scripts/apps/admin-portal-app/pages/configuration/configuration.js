'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('configuration', {
            parent: 'admin',
            url: '/configuration',
            data: {
                pageTitle: 'Configuration',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'ConfigurationController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/configuration/configuration.html'
                }
            },
            resolve: {
                resolvedData: function(ConfigurationService) {
                    return ConfigurationService.get().then(
                        function success(configuration) {
                            return {
                                configuration: configuration
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
                    $translatePartialLoader.addPart('configuration');
                    return $translate.refresh();
                }
            }
        });
    });
})();
