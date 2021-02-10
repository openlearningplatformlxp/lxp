/**
 * Admin Assets Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminFeaturedSearchesController', function(resolvedPagedSearch, $scope, $state, $http) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'featuredSearch.featuredSearches.searchSearches.inputPlaceholder',
                    totalItemsCount: 'featuredSearch.featuredSearches.searchSearches.totalSearchesCount'
                }
            },

            addKeyword = function() {

            },
            edit = function(featuredSearch, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('featured-searches-edit', {
                    id: featuredSearch.featuredSearchId
                });
            },
            deleteSearch = function(featuredSearch, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $http.delete('api/admin/featured-searches/' + featuredSearch.featuredSearchId).then(
                    function success(response) {
                        resolvedPagedSearch.reload();
                        return {};
                    }
                );
            };

        return {
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions,
            addKeyword: addKeyword,
            deleteSearch: deleteSearch,
            edit: edit
        };
    });
})();
