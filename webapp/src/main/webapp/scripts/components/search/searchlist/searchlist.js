/**
 * Search List container
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('searchlist',
        function() {
            return {
                bindToController: {
                    searchState: '=',
                    textLoading: '@',
                    textNoResults: '@'
                },
                controller: 'SearchListController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/search/searchlist/searchlist.html',
                transclude: true
            }
        }
    );

    module.controller('SearchListController',
        function() {
            var pagedSearch = this.searchState,
                textLoading = this.textLoading,
                textNoResults = this.textNoResults;

            return {
                pagedSearch: pagedSearch,
                textLoading: textLoading,
                textNoResults: textNoResults
            };
        }
    );
})();
