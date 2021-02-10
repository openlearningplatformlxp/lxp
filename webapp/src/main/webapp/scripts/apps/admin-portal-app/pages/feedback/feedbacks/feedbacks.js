'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.config(function($stateProvider) {
        $stateProvider.state('feedbacks', {
            parent: 'admin',
            url: '/feedbacks',
            data: {
                pageTitle: 'Feedbacks',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: 'AdminFeedbacksController',
                    controllerAs: 'ctrl',
                    templateUrl: 'scripts/apps/admin-portal-app/pages/feedback/feedbacks/feedbacks.html'
                }
            },
            resolve: {
                resolvedPagedSearch: function(SearchService) {
                    return SearchService.getPagedSearchInstancePromise('api/admin/feedbacks/search', {
                        initialSearchValue: '',
                        initialSort: {
                            field: 'createdDate',
                            dir: 'DESC'
                        },
                        sortFields: ['type', 'createdDate']
                    }).then(function(pagedSearch) {
                        return pagedSearch;
                    });
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('feedbacks');
                    return $translate.refresh();
                }
            }
        });
    });
})();
