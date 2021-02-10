/**
 * Admin Assets Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminWikipageController', function(resolvedPagedSearch, $scope, $state) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'wikipage.searchWikipages.inputPlaceholder',
                    totalItemsCount: 'wikipage.searchWikipages.totalWikipagesCount'
                },
            },
            data = {

            },

            goToCreateWikipage = function() {
                $state.go('wikipage-create');
            },
            goToWikipageTree = function() {
                $state.go('wikipage-tree');
            },

            goToEditWikipage = function(wikipage, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('wikipage-edit', {
                    id: wikipage.id
                });
            };
        return {
            data: data,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions,
            goToCreateWikipage: goToCreateWikipage,
            goToWikipageTree: goToWikipageTree,
            goToEditWikipage: goToEditWikipage
        };
    });
})();
