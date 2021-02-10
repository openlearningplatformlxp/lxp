/**
 * User Roles Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminUserRolesController', function(resolvedPagedSearch, $state, Principal) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'user-role.user-roles.searchRoles.inputPlaceholder',
                    totalItemsCount: 'user-role.user-roles.searchRoles.totalRolesCount'
                }
            },

            data = {
                authorities: Principal.getAuthorities()
            },

            displayName = function(role) {
                return mangleName(role.name);
            },

            editRole = function(role, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('user-role-edit', {
                    roleId: mangleName(role.name)
                });
            },

            gotoNewRole = function() {
                $state.go('user-role-add');
            },

            mangleName = function(roleName) {
                return roleName.replace(/^ROLE_/, '');
            },

            zend;

        return {
            data: data,
            displayName: displayName,
            editRole: editRole,
            gotoNewRole: gotoNewRole,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions
        };
    });
})();
