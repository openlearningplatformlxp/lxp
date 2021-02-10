/**
 * Admin Spring Session List Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminSpringSessionListController',
        function(resolvedPagedSearch) {
            var searchbarOptions = {
                    textKeys: {
                        searchInputPlaceholder: 'spring-session-list.searchSessions.inputPlaceholder',
                        totalItemsCount: 'spring-session-list.searchSessions.totalSessionsCount'
                    }
                },

                zend;

            return {
                pagedSearch: resolvedPagedSearch,
                searchbarOptions: searchbarOptions
            };
        }
    );
})();
