/**
 * Admin Assets Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminAuditSearchesController', function(resolvedLastIndex, resolvedPagedSearch, $scope, $state, $http) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'auditSearch.auditSearches.searchSearches.inputPlaceholder',
                    totalItemsCount: 'auditSearch.auditSearches.searchSearches.totalSearchesCount'
                }
            },
            data = {
                lastIndex: resolvedLastIndex,
                indexEnabled: resolvedLastIndex.indexEnabled
            },
            triggerReindex = function() {
                if (data.indexEnabled) {
                    data.indexEnabled = false;
                    $http({
                        url: 'api/admin/audit-searches/reindex',
                        method: 'POST'
                    }).then(function success(response) {
                        data.lastIndex = {
                                time: new Date().getTime()
                            }
                    }, function error(response) {
                        data.indexEnabled = true;
                        console.log('Failed to reindex')
                    });
                }
            };

        return {
            data: data,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions,
            triggerReindex: triggerReindex,
        };
    });
})();
