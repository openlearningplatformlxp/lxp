/**
 * Selected Items service.
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('selectedItemsCheckbox',
        function() {
            return {
                bindToController: {
                    itemId: '=',
                    options: '=',
                    state: '='
                },
                controller: 'SelectedItemsCheckboxController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/selected-items/selected-items.html'
            }
        }
    );

    module.controller('SelectedItemsCheckboxController',
        function() {
            var itemId = this.itemId,
                opts = angular.copy(this.options) || {},
                selectedItems = this.state,

                init = function() {

                },

                zend;

            init();

            return {
                selectedItems: selectedItems
            }
        }
    );

    module.service('SelectedItemsService', function($timeout, $translate, AlertsService) {
        var selectedItems = function(options) {
            var opts = angular.copy(options) || {},
                items = {},
                selectedCount = undefined,
                state = {
                    allSelected: false,
                    visible: false
                },

                clear = function(confirm) {
                    if (confirm) {
                        AlertsService.confirm({
                            buttonOk: {
                                class: 'btn-danger',
                                onClick: _clear,
                                text: $translate.instant('global.services.selected-items.clear.clearSelections')
                            },
                            text: $translate.instant('global.services.selected-items.clear.clearSelectionsText', {
                                selectedCount: getSelectedCount
                            }),
                            title: $translate.instant('global.services.selected-items.clear.clearSelectionsTitle')
                        });
                    } else {
                        _clear();
                    }
                },

                _clear = function() {
                    items = {};
                    selectedCount = undefined;
                    state.allSelected = false;
                },

                computeAndSetAllSelected = function() {
                    var key,
                        allCurrentSelected = true,
                        anyCurrent = false;

                    for (key in items) {
                        if (items[key].current) {
                            anyCurrent = true;

                            if (!items[key].selected) {
                                allCurrentSelected = false;
                                break;
                            }
                        }
                    }

                    state.allSelected = (anyCurrent && allCurrentSelected);
                },

                computeSelectedCount = function() {
                    var count = 0;

                    angular.forEach(items, function(item) {
                        if (item.selected) {
                            count++;
                        }
                    });

                    return count;
                },

                deselect = function(id) {
                    set(id, false);
                },

                deselectAll = function() {
                    angular.forEach(items, function(item) {
                        if (item.current) {
                            deselect(item.id);
                        }
                    });
                },

                get = function(id) {
                    var item = items[id];

                    if (!item) {
                        item = {
                            id: id,
                            selected: false
                        };

                        items[id] = item;
                    }

                    item.current = true;

                    return item;
                },

                getState = function() {
                    return state;
                },

                getSelectedCount = function() {
                    if (angular.isUndefined(selectedCount)) {
                        selectedCount = computeSelectedCount();
                    }

                    return selectedCount;
                },

                getSelectedIds = function() {
                    var ids = [];

                    angular.forEach(items, function(item) {
                        if (item.selected) {
                            ids.push(item.id);
                        }
                    });

                    return ids;
                },

                hasSelected = function() {
                    return (getSelectedCount() > 0);
                },

                init = function() {
                    startPage();
                },

                isSelected = function(id) {
                    var item = get(id);

                    return item.selected;
                },

                onItemChange = function() {
                    selectedCount = undefined;

                    computeAndSetAllSelected();
                },

                select = function(id) {
                    set(id, true);
                },

                selectAll = function() {
                    angular.forEach(items, function(item) {
                        if (item.current) {
                            select(item.id);
                        }
                    });
                },

                set = function(id, selected) {
                    var item = get(id);

                    if (selected) {
                        if (angular.isDefined(selectedCount) && !item.selected) {
                            selectedCount++;
                        }
                    } else {
                        if (angular.isDefined(selectedCount) && item.selected) {
                            selectedCount--;
                        }
                    }

                    item.selected = selected;

                    if (selected) {
                        if (!state.allSelected) {
                            computeAndSetAllSelected(); // need to recompute
                        }
                    } else {
                        state.allSelected = false;
                    }
                },

                setAll = function(select) {
                    if (select) {
                        selectAll();
                    } else {
                        deselectAll();
                    }
                },

                startPage = function() {
                    angular.forEach(items, function(item) {
                        item.current = false;
                    });

                    $timeout(function() {
                        // $timeout causes this to run after page rendering, which ensures items have been set-to-current before computing.

                        computeAndSetAllSelected();
                    });
                },

                toggle = function(id) {
                    var item = get(id);

                    set(id, !item.selected);
                },

                zend;

            init();

            return {
                clear: clear,
                deselect: deselect,
                deselectAll: deselectAll,
                get: get,
                getSelectedCount: getSelectedCount,
                getSelectedIds: getSelectedIds,
                getState: getState,
                hasSelected: hasSelected,
                isSelected: isSelected,
                onItemChange: onItemChange,
                select: select,
                selectAll: selectAll,
                setAll: setAll,
                startPage: startPage,
                toggle: toggle
            };
        };

        return {
            getSelectedItemsInstance: function(options) {
                return new selectedItems(options);
            }
        }
    });
})();
