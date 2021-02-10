/**
 * User Role Upsert Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminUserRoleAddEditController',
        function(resolvedData, $http, $state, $translate, AlertsService, MessagesService, Principal) {
            var data = {
                    authorities: Principal.getAuthorities(),
                    deleted: false,
                    isError: false,
                    authority: undefined,
                    permissionGroups: resolvedData.permissionGroups,
                    roleName: undefined,
                    roleNameOrig: undefined
                },

                pageMessages = MessagesService.getMessagesInstance(),

                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToAdminUsers
                            }
                        });
                    } else {
                        goToAdminUsers();
                    }
                },

                canDelete = function() {
                    return isEdit() && data.authority.deletable && data.authorities.USERS_ROLE_DELETE;
                },

                deleteRole = function() {
                    var deleteRole = function() {
                        $http.delete('api/admin/roles/' + resolvedData.authority.name).then(
                            function success(response) {
                                data.deleted = true;

                                pageMessages.addXlatedSuccess('user-role.user-role-add-edit.delete.success', {
                                    roleName: mangleRoleName(resolvedData.authority.name)
                                });
                            },
                            function error(response) {
                                pageMessages.addHttpError(response);
                            }
                        );
                    };

                    AlertsService.confirm({
                        buttonOk: {
                            class: 'btn-danger',
                            onClick: deleteRole,
                            text: $translate.instant('user-role.user-role-add-edit.buttons.delete')
                        },
                        text: $translate.instant('user-role.user-role-add-edit.delete.text', {
                            roleName: mangleRoleName(resolvedData.authority.name)
                        }),
                        title: '<span class="text-muted">' + $translate.instant('user-role.user-role-add-edit.delete.title', {
                            roleName: mangleRoleName(resolvedData.authority.name)
                        }) + '</span>'
                    });
                },

                goToAdminUsers = function() {
                    $state.go('user-roles');
                },

                hasActions = function() {
                    return canDelete();
                },

                init = function() {
                    if (resolvedData.httpError) {
                        data.isError = true;
                        pageMessages.addHttpError(resolvedData.httpError);
                    } else {
                        data.authority = processAuthority(resolvedData.authority);
                    }
                },

                isAdd = function() {
                    return !isEdit() && !data.isError;
                },

                isEdit = function() {
                    return resolvedData.authority && resolvedData.authority.name && !data.isError;
                },

                isEditable = function() {
                    return ((isAdd() && data.authorities.USERS_ROLE_CREATE) || (isEdit() && data.authorities.USERS_ROLE_UPDATE));
                },

                isFormDisabled = function(form) {
                    return form.$invalid || form.$pristine;
                },

                isLoaded = function() {
                    return angular.isDefined(data.authority);
                },

                mangleRoleName = function(roleName) {
                    return roleName.replace(/^ROLE_/, '');
                },

                processAuthority = function(authority) {
                    if (authority) {
                        authority = angular.copy(authority);
                    } else {
                        authority = {};
                    }

                    data.roleName = authority.name;
                    data.roleNameOrig = angular.copy(authority.name);

                    var permissionMap = {};

                    if (authority.permissions) {
                        angular.forEach(authority.permissions, function(permission) {
                            permissionMap[permission.groupName + '_' + permission.permissionName + '_' + permission.operation] = true;
                        });
                    }

                    angular.forEach(data.permissionGroups, function(permissionGroup) {
                        angular.forEach(permissionGroup.permissionNames, function(permissionName) {
                            angular.forEach(permissionName.permissions, function(permission) {
                                permission.selected = permissionMap[permission.groupName + '_' + permission.permissionName + '_' + permission.operation];
                            });
                        });
                    });

                    return authority;
                },

                save = function(form) {
                    pageMessages.reset();

                    var permissions = [];

                    angular.forEach(data.permissionGroups, function(permissionGroup) {
                        angular.forEach(permissionGroup.permissionNames, function(permissionName) {
                            angular.forEach(permissionName.permissions, function(permission) {
                                if (permission.selected) {
                                    permissions.push({
                                        groupName: permission.groupName,
                                        permissionName: permission.permissionName,
                                        operation: permission.operation
                                    });
                                }
                            });
                        });
                    });

                    var putData = {
                            roleName: (isAdd() ? 'ROLE_' : '') + data.authority.name.toUpperCase(),
                            permissionKeys: permissions
                        },
                        url = 'api/admin/roles' + (isEdit() ? '/' + resolvedData.authority.name : '');

                    $http.put(url, putData).then(function(response) {
                        resolvedData.authority = (isEdit() ? response.data : {});
                        data.authority = processAuthority(isEdit() ? resolvedData.authority : {});
                        pageMessages.addXlatedSuccess(isAdd() ? 'user-role.user-role-add-edit.messages.added' : 'user-role.user-role-add-edit.messages.edited');

                        form.$setPristine();
                        form.$setUntouched();
                    }, function(response) {
                        pageMessages.addHttpError(response);
                    });
                },

                zend;

            init();

            return {
                data: data,

                cancel: cancel,
                canDelete: canDelete,
                deleteRole: deleteRole,
                goToAdminUsers: goToAdminUsers,
                hasActions: hasActions,
                isAdd: isAdd,
                isEdit: isEdit,
                isEditable: isEditable,
                isFormDisabled: isFormDisabled,
                isLoaded: isLoaded,
                mangleRoleName: mangleRoleName,
                save: save,
                pageMessages: pageMessages
            };
        }
    );
})();
