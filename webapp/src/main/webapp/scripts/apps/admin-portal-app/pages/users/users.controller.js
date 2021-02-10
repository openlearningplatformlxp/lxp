/**
 * Admin Users Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminUsersController', function(resolvedPagedSearch, $http, $state, $translate, AlertsService) {
        var searchbarOptions = {
                showMultiSelect: true,
                textKeys: {
                    searchInputPlaceholder: 'user.users.searchUsers.inputPlaceholder',
                    totalItemsCount: 'user.users.searchUsers.totalUsersCount'
                }
            },

            deleteUsers = function() {
                var deleteUsers = function() {
                        $http.post('api/admin/persons/delete', resolvedPagedSearch.selected.getSelectedIds()).then(
                            function success(response) {
                                AlertsService.alert({
                                    text: $translate.instant(1 == selectedCount ? 'user.users.deleteUsers.success' : 'user.users.deleteUsers.plural.success'),
                                    title: $translate.instant('global.messages.success.title')
                                });

                                resolvedPagedSearch.reload();
                            },
                            function error(response) {
                                AlertsService.httpError(response);
                            }
                        );
                    },

                    selectedCount = resolvedPagedSearch.selected.getSelectedCount();

                AlertsService.confirm({
                    buttonOk: {
                        class: 'btn-danger',
                        onClick: deleteUsers,
                        text: $translate.instant(1 == selectedCount ? 'user.users.buttons.deleteUser' : 'user.users.buttons.deleteUsers', {
                            selectedCount: selectedCount
                        })
                    },
                    text: $translate.instant(1 == selectedCount ? 'user.users.deleteUsers.text' : 'user.users.deleteUsers.plural.text', {
                        selectedCount: selectedCount
                    }),
                    title: '<span class="text-muted">' + $translate.instant(1 == selectedCount ? 'user.users.deleteUsers.title' : 'user.users.deleteUsers.plural.title') + '</span>'
                });
            },

            editUser = function(user, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('user-edit', {
                    userId: user.id
                });
            },

            gotoNewUser = function() {
                $state.go('user-add');
            },

            zend;

        return {
            deleteUsers: deleteUsers,
            editUser: editUser,
            gotoNewUser: gotoNewUser,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions
        };
    });
})();
