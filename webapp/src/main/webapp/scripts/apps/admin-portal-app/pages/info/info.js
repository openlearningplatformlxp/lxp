/**
 * Node Info Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('info', {
            parent: 'admin',
            url: '/info?more',
            data: {
                pageTitle: 'Info'
            },
            views: {
                'content@site': {
                    controller: 'InfoController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/info/info.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/system/info').then(function(response) {
                        return {
                            generalInfo: response.data.general,
                            headerInfo: response.data.header,
                            propertyInfo: response.data.property,
                            more: !!$stateParams.more
                        };
                    }, function(response) {
                        return null;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('info');
                    return $translate.refresh();
                }
            }
        });
    });
})();
