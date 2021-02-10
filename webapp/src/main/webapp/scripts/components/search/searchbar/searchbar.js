/**
 * Searchbar
 *
 * Example:
 *
 *     In a controller:
 *
 *         var pagedSearch = SearchService.getPagedSearchInstance('api/admin/persons/search', {pageSize: 10, initialSearchValue: '', searchOptions: {type: 'audio', color: 'blue'}}),
 *             searchbarOptions = {
 *                 textKeys: {
 *                     searchInputPlaceholder: 'user.users.searchUsers.inputPlaceholder',
 *                     totalItemsCount: 'user.users.searchUsers.totalUsersCount'
 *                 },
 *                 searchOptions: [
 *                     {
 *                         choices: [
 *                             {name: 'All', value: 'all'},
 *                             {name: 'Audio', value: 'audio'},
 *                             {name: 'Document', value: 'document'},
 *                             {name: 'HTML', value: 'html'}
 *                         ],
 *                         name: 'type',
 *                         text: {
 *                             label: 'Type'
 *                         }
 *                     },
 *                     {
 *                         choices: [
 *                             {name: '', value: null},
 *                             {name: 'Red', value: 'red'},
 *                             {name: 'Blue', value: 'blue'}
 *                         ],
 *                         name: 'color',
 *                         text: {
 *                             label: 'Color'
 *                         }
 *                     }
 *                 ]
 *             };
 *
 *     In a template:
 *
 *             <searchbar search-state="ctrl.pagedSearch" options="ctrl.searchbarOptions"></searchbar>
 *
 *             ... generally a table of results will be here ...
 *
 *             <searchbar search-state="ctrl.pagedSearch" pager-only="true"></searchbar>
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('searchbar',
        function() {
            return {
                bindToController: {
                    options: '=',
                    pagerOnly: '=',
                    searchState: '='
                },
                controller: 'SearchBarController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/search/searchbar/searchbar.html'
            }
        }
    );

    module.controller('SearchBarController',
        function() {
            var pagedSearch = this.searchState,
                pagerOnly = !!this.pagerOnly,

                data = {
                    numResults: undefined,
                    pager: {
                        page: undefined,
                        pages: undefined,
                        totalPages: undefined
                    },
                    searchValue: pagedSearch.getSearchValue(),
                    searchOperation: undefined,
                    unique: (new Date()).getTime()
                },
                extraOptionValues = {},
                opts = angular.copy(this.options) || {},

                clearSearch = function() {
                    data.searchValue = '';

                    search();
                },

                findOption = function(searchOption, value) {
                    if (value == undefined) {
                        return value;
                    }

                    var theValue;

                    angular.forEach(searchOption.choices, function(choice) {
                        if (choice.value == value) {
                            theValue = choice;
                        }
                    });

                    if (!theValue) {
                        searchOption.choices.unshift({
                            name: '[' + value + ']',
                            value: value
                        });
                        extraOptionValues[searchOption.name] = true;

                        theValue = searchOption.choices[0];
                    }

                    return theValue;
                },

                getPage = function() {
                    return pagedSearch.getPage();
                },

                getPager = function() {
                    if (pagedSearch.hasResults() && pagedSearch.getTotalPages() > 3) {
                        if (data.pager.page != pagedSearch.getPage() || data.pager.totalPages != pagedSearch.getTotalPages()) {
                            data.pager = {
                                page: pagedSearch.getPage(),
                                pages: [],
                                pageSelected: undefined,
                                totalPages: pagedSearch.getTotalPages()
                            };

                            for (var i = 0; i < pagedSearch.getTotalPages(); i++) {
                                data.pager.pages.push({
                                    page: i,
                                    display: i + 1
                                });

                                if ((i + 1) == pagedSearch.getPage()) {
                                    data.pager.pageSelected = data.pager.pages[data.pager.pages.length - 1];
                                }
                            }
                        }
                    } else {
                        if (angular.isDefined(data.pages)) {
                            data.pager = {
                                page: undefined,
                                pages: undefined,
                                pageSelected: undefined,
                                totalPages: undefined
                            }
                        }
                    }

                    return data.pager;
                },

                getResults = function() {
                    return opts.numResults;
                },

                getSearchOperations = function() {
                    return opts.searchOperations;
                },

                getSearchOptions = function() {
                    return opts.searchOptions;
                },

                getTextKey = function(key) {
                    if (opts.textKeys && opts.textKeys[key]) {
                        return opts.textKeys[key];
                    } else if (opts.text) {
                        return 'global.showValue';
                    }
                },

                getTextValues = function(key) {
                    var hasXlate = (opts.textKeys && opts.textKeys[key]),
                        textValues = {};

                    if (key == 'pageXofY') {
                        textXlateAddValue(textValues, hasXlate, key, 'X', pagedSearch.getPage(), 'Y', pagedSearch.getTotalPages());
                    } else if (key == 'totalItemsCount') {
                        textXlateAddValue(textValues, hasXlate, key, 'count', pagedSearch.getTotalItems());
                    } else {
                        textXlateAddValue(textValues, hasXlate, key);
                    }

                    return textValues;
                },

                getTotalPages = function() {
                    return pagedSearch.getTotalPages();
                },

                hasPageNext = function() {
                    return pagedSearch.hasPageNext();
                },

                hasPagePrev = function() {
                    return pagedSearch.hasPagePrev();
                },

                hasResults = function() {
                    return pagedSearch.hasResults();
                },

                init = function() {
                    if (!angular.isArray(opts.numResults)) {
                        opts.numResults = [{
                                name: '10',
                                value: 10
                            },
                            {
                                name: '25',
                                value: 25
                            },
                            {
                                name: '50',
                                value: 50
                            },
                            {
                                name: '100',
                                value: 100
                            }
                        ];
                    }

                    angular.forEach(opts.numResults, function(result) {
                        if (result.value == pagedSearch.getPageSize()) {
                            data.numResults = result;
                        }
                    });

                    if (!data.numResults) {
                        data.numResults = opts.numResults[0];
                    }

                    if (!angular.isArray(opts.searchOperations)) {
                        opts.searchOperations = [{
                                name: 'Contains',
                                value: 'ilike'
                            },
                            {
                                name: 'Equals',
                                value: 'iequal'
                            },
                            {
                                name: 'Starts with',
                                value: 'istarts with'
                            },
                            {
                                name: 'Ends with',
                                value: 'iends with'
                            }
                        ];
                    }

                    angular.forEach(opts.searchOperations, function(operation) {
                        if (operation.value == pagedSearch.getSearchOperation()) {
                            data.searchOperation = operation;
                        }
                    });

                    if (!data.searchOperation) {
                        data.searchOperation = opts.searchOperations[0];
                    }

                    if (angular.isArray(opts.searchOptions)) {
                        angular.forEach(opts.searchOptions, function(searchOption) {
                            searchOption.data = {
                                value: findOption(searchOption, pagedSearch.getSearchOption(searchOption.name))
                            };
                        });
                    }

                    if (!opts.textKeys) {
                        opts.textKeys = {};
                    }

                    if (!opts.text) {
                        opts.text = {};
                    }

                    if (!opts.text.pageXofY && !opts.textKeys.pageXofY) {
                        opts.textKeys.pageXofY = 'global.directives.searchbar.pageXofY';
                    }

                    if (!opts.text.resultsLabel && !opts.textKeys.resultsLabel) {
                        opts.textKeys.resultsLabel = 'global.labels.Results';
                    }

                    if (!opts.text.searchButtonLabel && !opts.textKeys.searchButtonLabel) {
                        opts.textKeys.searchButtonLabel = 'global.labels.Search';
                    }

                    if (!opts.text.searchInputPlaceholder && !opts.textKeys.searchInputPlaceholder) {
                        opts.textKeys.searchInputPlaceholder = 'global.directives.searchbar.searchInputPlaceholder';
                    }

                    if (!opts.text.totalItemsCount && !opts.textKeys.totalItemsCount) {
                        opts.textKeys.totalItemsCount = 'global.directives.searchbar.totalItemsCount';
                    }

                    if (angular.isDefined(opts.searchbox)) {
                        opts.searchbox = !!opts.searchbox;
                    } else {
                        opts.searchbox = true;
                    }

                    if (angular.isDefined(opts.countPicker)) {
                        opts.countPicker = !!opts.countPicker;
                    } else {
                        opts.countPicker = true;
                    }

                    if (angular.isDefined(opts.pagePicker)) {
                        opts.pagePicker = !!opts.pagePicker;
                    } else if (isPagerOnly()) {
                        opts.pagePicker = true;
                    } else {
                        opts.pagePicker = false;
                    }

                    if (angular.isDefined(opts.searchOperationsSelector)) {
                        opts.searchOperationsSelector = !!opts.searchOperationsSelector;
                    } else {
                        opts.searchOperationsSelector = true;
                    }
                },

                isNotPagerOnly = function() {
                    return !pagerOnly;
                },

                isPagerOnly = function() {
                    return !!pagerOnly;
                },

                isSearching = function() {
                    return pagedSearch.isSearching();
                },

                onNumResultsChange = function() {
                    pagedSearch.setPageSize(data.numResults.value);
                    pagedSearch.search(data.searchValue);
                },

                onPagerChange = function() {
                    pagedSearch.setPage(data.pager.pageSelected.page);
                },

                onSearchOptionsChange = function(searchOption) {
                    var value = searchOption.data.value.value;

                    pagedSearch.setSearchOption(searchOption.name, value);
                    pagedSearch.search(data.searchValue);

                    if (extraOptionValues[searchOption.name]) {
                        searchOption.choices.splice(0, 1);
                        extraOptionValues[searchOption.name] = false;
                    }
                },

                pageNext = function() {
                    return pagedSearch.pageNext();
                },

                pagePrev = function() {
                    return pagedSearch.pagePrev();
                },

                search = function() {
                    pagedSearch.search(data.searchValue);
                },

                setSearchOperation = function(searchOperation) {
                    data.searchOperation = searchOperation;

                    pagedSearch.setSearchOperation(data.searchOperation.value);

                    search();
                },

                showCountPicker = function() {
                    return opts.countPicker;
                },

                showMultiSelect = function() {
                    return !!opts.showMultiSelect;
                },

                showPagePicker = function() {
                    return opts.pagePicker;
                },

                showSearchbox = function() {
                    return opts.searchbox;
                },

                showSearchOperationsSelector = function() {
                    return opts.searchOperationsSelector;
                },

                textXlateAddValue = function(newopts, hasXlate, key, name1, value1, name2, value2) {
                    if (hasXlate) {
                        if (angular.isString(name1)) {
                            newopts[name1] = value1;

                            if (angular.isString(name2)) {
                                newopts[name2] = value2;
                            }
                        }
                    } else if (opts.text) {
                        if (angular.isString(name1)) {
                            newopts['value'] = textReplaceValue(opts.text[key], name1, value1);

                            if (angular.isString(name2)) {
                                newopts['value'] = textReplaceValue(newopts['value'], name2, value2);
                            }
                        } else {
                            newopts['value'] = opts.text[key];
                        }
                    }
                },

                textReplaceValue = function(text, name, value) {
                    if (angular.isString(text) && angular.isString(name)) {
                        return text.replace(new RegExp('{{' + name + '}}', 'g'), value);
                    } else {
                        return text;
                    }
                };

            init();

            return {
                data: data,

                clearSearch: clearSearch,
                getPage: getPage,
                getPager: getPager,
                getResults: getResults,
                getSearchOperations: getSearchOperations,
                getSearchOptions: getSearchOptions,
                getTextKey: getTextKey,
                getTextValues: getTextValues,
                getTotalPages: getTotalPages,
                hasPageNext: hasPageNext,
                hasPagePrev: hasPagePrev,
                hasResults: hasResults,
                isNotPagerOnly: isNotPagerOnly,
                isPagerOnly: isPagerOnly,
                isSearching: isSearching,
                onNumResultsChange: onNumResultsChange,
                onPagerChange: onPagerChange,
                onSearchOptionsChange: onSearchOptionsChange,
                pageNext: pageNext,
                pagePrev: pagePrev,
                search: search,
                selected: pagedSearch.selected,
                setSearchOperation: setSearchOperation,
                showCountPicker: showCountPicker,
                showMultiSelect: showMultiSelect,
                showPagePicker: showPagePicker,
                showSearchbox: showSearchbox,
                showSearchOperationsSelector: showSearchOperationsSelector
            };
        }
    );
})();
