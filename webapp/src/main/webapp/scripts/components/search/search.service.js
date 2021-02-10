/**
 * Search service.
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('SearchService', function($http, $q, AlertsService, SelectedItemsService) {
        var pagedSearch = function(endpoint, options) {
            var opts = angular.copy(options) || {},
                addEmptyResultsIfPageSizeLessThanEqual = opts.addEmptyResultsIfPageSizeLessThanEqual || 25,
                addEmptyResultsIfSinglePage = !!opts.addEmptyResultsIfSinglePage,
                page = 0,
                pageData = undefined,
                pageSize = opts.pageSize || 25,
                searching = false,
                searchOperation = 'ilike',
                searchValue = '',
                selectedItems = SelectedItemsService.getSelectedItemsInstance(),
                sort = {},
                sortSecondary = {},
                sortFields = {},

                addEntry = function(entry) {
                    if (!isLoaded()) {
                        return false; // Not supported yet!
                    }

                    if (!angular.isArray(pageData.content)) {
                        return false; // Not supported yet!
                    }

                    pageData.content.unshift(entry);
                },

                getContent = function() {
                    if (pageData && angular.isArray(pageData.content)) {
                        return pageData.content;
                    }

                    return [];
                },

                getOptions = function() {
                    return opts;
                },

                getPage = function() {
                    return page + 1;
                },

                getPageSize = function() {
                    return pageSize;
                },

                getSearchOperation = function() {
                    return searchOperation;
                },

                getSearchOption = function(name) {
                    return opts.searchOptions[name];
                },

                getSearchValue = function() {
                    return searchValue;
                },

                getSortDir = function(field) {
                    if (angular.isString(field) && field === sort.field) {
                        return sort.dir;
                    }

                    return undefined;
                },

                getSortSecondaryDir = function(field) {
                    if (angular.isString(field) && field === sortSecondary.field) {
                        return sortSecondary.dir;
                    }

                    return undefined;
                },

                getTotalPages = function() {
                    if (isNotLoaded()) {
                        return undefined;
                    }

                    return pageData.totalPages;
                },

                getTotalItems = function() {
                    if (isNotLoaded()) {
                        return undefined;
                    }

                    return pageData.totalElements;
                },

                hasResults = function() {
                    return (isLoaded() && getTotalItems() > 0);
                },

                hasEmptyResults = function() {
                    return (isLoaded() && getTotalItems() === 0);
                },

                hasPageNext = function() {
                    if (isNotLoaded()) {
                        return false;
                    }

                    return (page < pageData.totalPages - 1);
                },

                hasPagePrev = function() {
                    return (page > 0);
                },

                init = function() {
                    opts.inputPlaceholder = opts.inputPlaceholder || '';
                    opts.searchData = opts.searchData || {};
                    opts.onSuccess = opts.onSuccess || angular.noop;

                    if (!angular.isObject(opts.searchOptions)) {
                        opts.searchOptions = {};
                    }

                    if (angular.isArray(opts.sortFields)) {
                        angular.forEach(opts.sortFields, function(sortField) {
                            if (angular.isString(sortField) && sortField.length > 0) {
                                sortFields[sortField] = true;
                            }
                        });
                    }

                    if (angular.isObject(opts.initialSort) && angular.isString(opts.initialSort.field) && opts.initialSort.field.length > 0) {
                        sort.field = opts.initialSort.field;
                        sort.dir = opts.initialSort.dir || 'ASC';

                        if (angular.isObject(opts.initialSortSecondary) && angular.isString(opts.initialSortSecondary.field) && opts.initialSortSecondary.field.length > 0) {
                            sortSecondary.field = opts.initialSortSecondary.field;
                            sortSecondary.dir = opts.initialSortSecondary.dir || 'ASC';
                        }
                    }

                    if (angular.isString(opts.initialSearchOperation)) {
                        data.searchOperation = opts.initialSearchOperation;
                    }

                    if (angular.isString(opts.initialSearchValue)) {
                        search(opts.initialSearchValue);
                    }

                    if (!angular.isFunction(opts.onError)) {
                        opts.onError = function(results) {
                            AlertsService.httpError(results);
                        };
                    }
                },

                isLoaded = function() {
                    return !!pageData;
                },

                isNotLoaded = function() {
                    return !isLoaded();
                },

                isSearching = function() {
                    return searching;
                },

                isSortable = function(field) {
                    return !!sortFields[field];
                },

                pageNext = function() {
                    if (hasPageNext()) {
                        setPage(page + 1);
                    }
                },

                pagePrev = function() {
                    if (hasPagePrev()) {
                        setPage(page - 1);
                    }
                },

                reload = function(samePage) {
                    _search(!samePage);
                },

                search = function(value) {
                    searchValue = value;

                    return _search(true);
                },

                setPage = function(newPage) {
                    if (newPage >= 0 && newPage < getTotalPages()) {
                        page = newPage;

                        _search();
                    }
                },

                setPageSize = function(size) {
                    pageSize = size;
                },

                setSearchOperation = function(value) {
                    searchOperation = value;
                },

                setSearchOption = function(name, value) {
                    opts.searchOptions[name] = value;
                },

                toggleSort = function(field) {
                    var sortDir = getSortDir(field);

                    if (sortDir === 'ASC') {
                        sort.dir = 'DESC';
                    } else if (sortDir === 'DESC') {
                        sort.dir = 'ASC';
                    } else if (isSortable(field)) {
                        sortSecondary = {
                            dir: sort.dir,
                            field: sort.field
                        };

                        sort.dir = 'ASC';
                        sort.field = field;
                    }
                },

                _search = function(newSearch) {
                    var searchData = angular.copy(opts.searchData),
                        sortOrders = [];

                    searching = true;

                    searchData.searchValue = searchValue;
                    searchData.searchOperation = searchOperation;
                    searchData.page = (newSearch ? 0 : page);
                    searchData.pageSize = pageSize;

                    if (angular.isObject(opts.searchOptions)) {
                        var options = {},
                            optionsSize = 0;

                        angular.forEach(opts.searchOptions, function(value, name) {
                            if (name != null && value != null) {
                                options[name] = value;
                                optionsSize++;
                            }

                            if (optionsSize > 0) {
                                searchData.options = options;
                            }
                        });
                    }

                    if (angular.isString(sort.field) && sort.field.length > 0) {
                        sortOrders.push({
                            field: sort.field,
                            direction: sort.dir
                        });

                        if (angular.isString(sortSecondary.field) && sortSecondary.field.length > 0) {
                            sortOrders.push({
                                field: sortSecondary.field,
                                direction: sortSecondary.dir
                            });
                        }

                        searchData.sortOrders = sortOrders;
                    }

                    return $http.post(endpoint, searchData).then(
                        function success(response) {
                            searching = false;

                            pageData = response.data;

                            if (pageSize <= addEmptyResultsIfPageSizeLessThanEqual) {
                                if (addEmptyResultsIfSinglePage || pageData.totalPages > 1) {
                                    for (var i = response.data.content.length; i < pageSize; i++) {
                                        response.data.content.push({
                                            '_empty_': true
                                        });
                                    }
                                }
                            }

                            if (newSearch) {
                                page = 0;
                                selectedItems.clear();
                            }

                            selectedItems.startPage();

                            opts.onSuccess();
                        },
                        function error(response) {
                            searching = false;

                            opts.onError(response);
                        }
                    );
                };

            init();

            return {
                addEntry: addEntry,
                getContent: getContent,
                getOptions: getOptions,
                getPage: getPage,
                getPageSize: getPageSize,
                getSearchOperation: getSearchOperation,
                getSearchOption: getSearchOption,
                getSearchValue: getSearchValue,
                getSortDir: getSortDir,
                getSortSecondaryDir: getSortSecondaryDir,
                getTotalItems: getTotalItems,
                getTotalPages: getTotalPages,
                hasResults: hasResults,
                hasEmptyResults: hasEmptyResults,
                hasPageNext: hasPageNext,
                hasPagePrev: hasPagePrev,
                init: init,
                isSearching: isSearching,
                isSortable: isSortable,
                pageNext: pageNext,
                pagePrev: pagePrev,
                reload: reload,
                search: search,
                selected: selectedItems,
                setPage: setPage,
                setPageSize: setPageSize,
                setSearchOperation: setSearchOperation,
                setSearchOption: setSearchOption,
                toggleSort: toggleSort
            };
        };

        return {
            getPagedSearchInstance: function(endpoint, options) {
                return new pagedSearch(endpoint, options);
            },

            getPagedSearchInstancePromise: function(endpoint, options) {
                var deferred = $q.defer(),
                    opts = angular.copy(options) || {},
                    searchValue = opts.initialSearchValue;

                opts.initialSearchValue = undefined;

                var pagedSearchInstance = new pagedSearch(endpoint, opts);

                if (angular.isString(searchValue)) {
                    pagedSearchInstance.search(searchValue).then(
                        function success() {
                            deferred.resolve(pagedSearchInstance);
                        },
                        function error() {
                            deferred.resolve(pagedSearchInstance);
                        }
                    );
                } else {
                    deferred.resolve(pagedSearchInstance);
                }

                return deferred.promise;
            }
        }
    });
})();
