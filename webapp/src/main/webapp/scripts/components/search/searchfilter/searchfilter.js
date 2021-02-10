/**
 * Search Filter container
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('searchfilter',
        function() {
            return {
                bindToController: {
                    isarray: '=',
                    title: '@',
                    open: '=',
                    values: '=',
                    tags: '=',
                    filters: '=',
                    filter: '@',
                    refresh: '&'
                },
                controller: 'SearchFilterController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/search/searchfilter/searchfilter.html',
                transclude: true
            }
        }
    );

    module.controller('SearchFilterController',
        function() {
            var title = this.title,
                values = this.values,
                tags = this.tags,
                filters = this.filters,
                filter = this.filter,
                selected = filters[filter],
                open = this.open;

            var
                select = function(value, isTag) {
                    if (this.isarray) {
                        if (filters[filter] == null) {
                            filters[filter] = [];
                        }
                        var index = filters[filter].indexOf(value);
                        if (isTag) {
                            index = -1;
                            var j = 0;
                            if (filters[filter] != null) {
                                filters[filter].forEach(function(tag) {
                                    if (value.id === tag.id) {
                                        index = j;
                                    }
                                    j++;
                                });
                            }
                        }

                        if (index >= 0) {
                            filters[filter].splice(index, 1);
                        } else {
                            filters[filter].push(value);
                        }
                    } else {
                        filters[filter] = value;
                    }
                    this.refresh();
                },
                selectAll = function() {
                    filters[filter] = null;
                    this.refresh();
                },
                isTagActive = function(tag) {
                    if (this.isarray) {
                        var found = false;
                        if (filters[filter] != null) {
                            filters[filter].forEach(function(value) {
                                if (value.id === tag.id) {
                                    found = true;
                                }
                            });
                        }
                        return found;
                    } else {
                        return filters[filter] != null && filters[filter].id == tag.id;
                    }
                },
                toggleContent = function() {
                    this.open = !this.open;
                };

            return {
                open: open,
                selected: selected,
                title: title,
                values: values,
                tags: tags,
                select: select,
                selectAll: selectAll,
                isTagActive: isTagActive,
                toggleContent: toggleContent
            };
        }
    );
})();
