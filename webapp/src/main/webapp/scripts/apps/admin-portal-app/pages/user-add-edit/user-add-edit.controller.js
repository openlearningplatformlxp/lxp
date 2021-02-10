/**
 * Admin User Edit Controller.
 *
 * TODO: SAC: after save is performed, remove the success message if any field is modified (not error message though - they might be using that still)
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminUserAddEditController',
        function(resolvedData, $http, $state, $translate, AlertsService, ImpersonateUserService, MessagesService) {
            var data = {
                    availableAuthorities: undefined,
                    deleted: false,
                    isError: false,
                    sendActivationEmail: false,
                    setPassword: false,
                    user: undefined,
                    userAuthorities: [],
                    userDisplayName: undefined,
                    userLogin: undefined,
                    userOrig: undefined
                },

                pageMessages = MessagesService.getMessagesInstance(),
                authorityMap = {},

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
                    return isEdit();
                },

                canImpersonate = function() {
                    return (resolvedData.appSecurityImpersonateEnabled && isEdit() && !!data.userLogin);
                },

                cleanStr = function(value) {
                    if (angular.isString(value)) {
                        return value;
                    }

                    return '';
                },

                deleteUser = function() {
                    var deleteUser = function() {
                        $http.delete('api/admin/persons/' + data.user.id).then(
                            function success(response) {
                                data.deleted = true;

                                pageMessages.addXlatedSuccess('user.user-add-edit.deleteUser.success');
                            },
                            function error(response) {
                                pageMessages.addHttpError(response);
                            }
                        );
                    };

                    AlertsService.confirm({
                        buttonOk: {
                            class: 'btn-danger',
                            onClick: deleteUser,
                            text: $translate.instant('user.user-add-edit.buttons.deleteUser')
                        },
                        text: $translate.instant('user.user-add-edit.deleteUser.text', {
                            username: data.userDisplayName
                        }),
                        title: '<span class="text-muted">' + $translate.instant('user.user-add-edit.deleteUser.title') + '</span> ' + data.userDisplayName
                    });
                },

                getDisplayName = function(user) {
                    var displayName = cleanStr(user.firstName) + ' ' + cleanStr(user.middleInitial) + ' ' + cleanStr(user.lastName);

                    if (!angular.isString(displayName)) {
                        displayName = '';
                    }

                    displayName = displayName.replace(/  /g, ' ');

                    if (!displayName) {
                        displayName = cleanStr(user.login);
                    }

                    if (!displayName) {
                        displayName = '(' + user.id + ')';
                    }

                    return displayName;
                },

                goToAdminUsers = function() {
                    $state.go('users');
                },

                hasActions = function() {
                    return canDelete() || canImpersonate();
                },

                hasAuthority = function(authorityName) {
                    return (authorityMap[authorityName] || false);
                },

                impersonate = function() {
                    ImpersonateUserService.impersonate({
                        userDisplayName: data.userDisplayName,
                        userLogin: data.userLogin
                    });
                },

                init = function() {
                    if (resolvedData.httpError) {
                        data.isError = true;
                        pageMessages.addHttpError(resolvedData.httpError);
                    } else {
                        data.availableAuthorities = processAvailableAuthorities(resolvedData.availableAuthorities);
                        data.user = processUser(resolvedData.user);
                    }
                },

                isAdd = function() {
                    return !isEdit() && !data.isError;
                },

                isEdit = function() {
                    return angular.isDefined(resolvedData.userId) && !data.isError;
                },

                isFormDisabled = function(form) {
                    if (data.setPassword) {
                        if (!data.user.password || !data.confirmPassword) {
                            return true;
                        } else if (data.user.password != data.confirmPassword) {
                            return true;
                        }
                    }

                    return form.$invalid || form.$pristine;
                },

                isLoaded = function() {
                    return angular.isDefined(data.user);
                },

                makeActivationUrl = function() {
                    return ('#/activate?key=' + data.user.activationToken);
                },

                processAuthorityNameForDisplay = function(authorityName) {
                    if (!angular.isString(authorityName)) {
                        return authorityName
                    }

                    var processedName = authorityName.replace(/^ROLE_/, "");

                    if (processedName.length > 1) {
                        processedName = processedName.toLowerCase();
                        processedName = processedName.replace(/_/g, ' ');
                        processedName = processedName.substring(0, 1).toUpperCase() + processedName.substring(1);
                    }

                    return processedName;
                },

                processUser = function(user) {
                    data.userAuthorities = [];
                    authorityMap = {};

                    if (user && angular.isArray(user.authorities)) {
                        angular.forEach(user.authorities, function(authority) {
                            authorityMap[authority] = true;
                        });
                    } else if (!user) {
                        user = {};
                    }

                    data.userLogin = user.login;
                    data.userDisplayName = getDisplayName(user);

                    angular.forEach(data.availableAuthorities, function(authority) {
                        data.userAuthorities[authority.name] = hasAuthority(authority.name);
                    });

                    data.userOrig = angular.copy(user);

                    return user;
                },

                processAvailableAuthorities = function(availableAuthorities) {
                    if (!angular.isArray(availableAuthorities) || availableAuthorities.length < 1) {
                        return undefined;
                    }

                    var authorities = [];

                    angular.forEach(availableAuthorities, function(authorityName) {
                        var authority = {
                            id: 'authority' + authorityName, // TODO: SAC: process or 'escape' authorityName to make it safe
                            name: authorityName,
                            display: processAuthorityNameForDisplay(authorityName)
                        };

                        authorities.push(authority);

                        data.userAuthorities[authorityName] = hasAuthority(authorityName);
                    });

                    return authorities;
                },

                save = function(form) {
                    pageMessages.reset();

                    var authorities = [];

                    angular.forEach(data.availableAuthorities, function(authority) {
                        if (data.userAuthorities[authority.name]) {
                            authorities.push(authority.name);
                        }
                    });

                    var putData = {
                            id: data.user.id,
                            login: data.user.login,
                            activated: data.user.activated,
                            disabled: data.user.disabled,
                            firstName: data.user.firstName,
                            lastName: data.user.lastName,
                            email: data.user.email,
                            authorities: authorities
                        },
                        queryParams = {
                            sendActivationEmail: data.sendActivationEmail
                        },
                        url = 'api/admin/persons' + (isEdit() ? '/' + resolvedData.userId : '');

                    if (data.setPassword) {
                        putData.password = data.user.password;
                    }

                    // TODO: SAC: disable save button while request is outstanding
                    $http.put(url, putData, {
                        params: queryParams
                    }).then(
                        function success(response) {
                            data.user = processUser(isEdit() ? response.data : {});
                            data.setPassword = false;
                            pageMessages.addSuccess(isAdd() ? 'User has been created.' : 'User has been updated.'); // TODO: SAC: xlate

                            form.$setPristine();
                            form.$setUntouched();
                        },
                        function error(response) {
                            pageMessages.addHttpError(response);
                        }
                    );
                },

                zend;

            init();

            return {
                data: data,

                cancel: cancel,
                canDelete: canDelete,
                canImpersonate: canImpersonate,
                deleteUser: deleteUser,
                getDisplayName: getDisplayName,
                goToAdminUsers: goToAdminUsers,
                hasActions: hasActions,
                impersonate: impersonate,
                isAdd: isAdd,
                isEdit: isEdit,
                isFormDisabled: isFormDisabled,
                isLoaded: isLoaded,
                makeActivationUrl: makeActivationUrl,
                save: save,
                pageMessages: pageMessages
            };
        }
    );
})();
