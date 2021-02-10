/**
 * Search Sort - column sort selectors
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('searchsort',
        function() {
            return {
                bindToController: {
                    field: '@',
                    label: '@',
                    searchState: '='
                },
                controller: 'SearchSortController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/search/searchsort/searchsort.html'
            }
        }
    );

    module.controller('SearchSortController',
        function() {
            var field = this.field,
                label = this.label,
                pagedSearch = this.searchState,

                getLabel = function() {
                    return label;
                },

                isNotSortable = function() {
                    return !isSortable();
                },

                isSortable = function() {
                    return pagedSearch.isSortable(field);
                },

                isSortAsc = function() {
                    return isSortable && pagedSearch.getSortDir(field) === 'ASC';
                },

                isSortDesc = function() {
                    return isSortable && pagedSearch.getSortDir(field) === 'DESC';
                },

                isSortSecondaryAsc = function() {
                    return isSortable && pagedSearch.getSortSecondaryDir(field) === 'ASC';
                },

                isSortSecondaryDesc = function() {
                    return isSortable && pagedSearch.getSortSecondaryDir(field) === 'DESC';
                },

                sort = function() {
                    pagedSearch.toggleSort(field);
                    pagedSearch.reload();
                };

            return {
                getLabel: getLabel,
                isNotSortable: isNotSortable,
                isSortable: isSortable,
                isSortAsc: isSortAsc,
                isSortDesc: isSortDesc,
                isSortSecondaryAsc: isSortSecondaryAsc,
                isSortSecondaryDesc: isSortSecondaryDesc,
                sort: sort
            };
        }
    );
})();
