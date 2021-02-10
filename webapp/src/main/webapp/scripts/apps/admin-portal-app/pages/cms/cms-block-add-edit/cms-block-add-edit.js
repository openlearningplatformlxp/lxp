'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('cms-block-add', {
            parent: 'admin',
            url: '/cms-block-add',
            data: {
                pageTitle: 'Create CMS Block',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.cms-blocks',
                        stateName: 'cms-blocks'
                    },
                    {
                        name: 'global.breadcrumbs.cms-block-add',
                        stateName: 'cms-block-add'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminCmsBlockAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/cms/cms-block-add-edit/cms-block-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http) {
                    return $http.get('api/cms/pages/cms-block-upsert').then(
                        function success(response) {
                            return response.data;
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cms-blocks');
                    return $translate.refresh();
                }
            }
        });

        $stateProvider.state('cms-block-edit', {
            parent: 'admin',
            url: '/cms-block-edit/{cmsBlockId:int}',
            data: {
                pageTitle: 'Edit CMS Block',
                authorities: ['ROLE_ADMIN'],
                breadcrumbs: [{
                        name: 'global.breadcrumbs.home',
                        stateName: 'home'
                    },
                    {
                        name: 'global.breadcrumbs.cms-blocks',
                        stateName: 'cms-blocks'
                    },
                    {
                        name: 'global.breadcrumbs.cms-block-edit',
                        stateName: 'cms-block-edit'
                    }
                ]
            },
            views: {
                'content@site': {
                    controller: 'AdminCmsBlockAddEditController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/cms/cms-block-add-edit/cms-block-add-edit.html'
                }
            },
            resolve: {
                resolvedData: function($http, $stateParams) {
                    return $http.get('api/cms/pages/cms-block-upsert/' + $stateParams.cmsBlockId).then(
                        function success(response) {
                            return response.data;
                        },
                        function error(response) {
                            return {
                                httpError: response
                            };
                        }
                    );
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cms-blocks');
                    return $translate.refresh();
                }
            }
        });
    });
})();
