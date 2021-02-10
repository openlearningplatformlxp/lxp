'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('register', {
            parent: 'account',
            url: '/register?key',
            data: {
                pageTitle: 'Settings',
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'RegisterController',
                    controllerAs: 'ctrl',
                    resolve: {
                        resolvedData: function($http, $state, $stateParams) {
                            if (!$stateParams.key) {
                                return {};
                            }

                            return $http.get('api/account/page/register', {
                                params: {
                                    key: $stateParams.key
                                }
                            }).then(
                                function success(response) {
                                    if ($stateParams.key && !response.data.person) {
                                        $state.go('login');

                                        return undefined;
                                    }

                                    return response.data;
                                },
                                function error(response) {
                                    return {
                                        httpError: response
                                    };
                                }
                            );
                        }
                    },
                    templateUrl: 'scripts/pages/account/register/register.html'
                }
            },
            resolve: {
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }
            }
        });
    });
})();