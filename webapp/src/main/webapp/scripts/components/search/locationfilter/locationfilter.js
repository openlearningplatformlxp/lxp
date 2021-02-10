/**
 * Search Filter container
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('locationfilter',
        function() {
            return {
                bindToController: {
                    title: '@',
                    open: '=',
                    values: '=',
                    tags: '=',
                    filters: '=',
                    filter: '@',
                    refresh: '&'
                },
                controller: 'LocationFilterController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/search/locationfilter/locationfilter.html',
                transclude: true
            }
        }
    );

    module.controller('LocationFilterController',
        function($scope) {
            var title = this.title,
                values = this.values,
                tags = this.tags,
                filters = this.filters,
                filter = this.filter,
                selected = filters[filter],
                open = this.open,
                countryIndex = null;

            var
                select = function(value) {
                    filters[filter] = value;
                    this.refresh();
                },
                selectAll = function() {
                    filters[filter] = null;
                    this.refresh();
                },
                toggleContent = function() {
                    this.open = !this.open;
                },
                onChangeCountry = function(countryIndex) {
                    for (var i = 0; i < this.tags.length; i++) {
                        if (this.filters['country'] == this.tags[i].country) {
                            this.countryIndex = i;
                            break;
                        }
                    }
                    this.filters['city'] = "";
                    this.refresh();
                },
                onChangeCity = function() {
                    this.refresh();
                };

            return {
                open: open,
                countryIndex: countryIndex,
                selected: selected,
                title: title,
                values: values,
                tags: tags,
                select: select,
                selectAll: selectAll,
                toggleContent: toggleContent,
                onChangeCountry: onChangeCountry,
                onChangeCity: onChangeCity
            };
        }
    );
})();
