'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('ImpersonateUserService', function($translate, AlertsService) {
        var impersonate = function(data) {
                AlertsService.confirm({
                    buttonOk: {
                        class: 'btn-danger',
                        text: $translate.instant('impersonate-user.modal.buttons.ok')
                    },
                    include: {
                        controller: 'ImpersonateUserModalController as ctrl',
                        resolve: {
                            resolvedData: function() {
                                return {
                                    userDisplayName: data.userDisplayName,
                                    userLogin: data.userLogin
                                };
                            }
                        },
                        templateUrl: 'scripts/components/impersonate-user/impersonate-user.modal.html'
                    },
                    title: '<span class="text-muted">' + $translate.instant('impersonate-user.modal.title') + '</span> ' + data.userDisplayName
                });
            },

            zend;

        return {
            impersonate: impersonate
        }
    });

    module.controller('ImpersonateUserModalController',
        function(resolvedData, $http, $translate, AlertsService, AlertsServiceDelegate) {
            var data = {
                    password: undefined,
                    userDisplayName: resolvedData.userDisplayName,
                    userLogin: resolvedData.userLogin
                },

                impersonate = function() {
                    $http.get('admin/impersonate/start?username=' + data.userLogin + '&password=' + data.password).then(
                        function success() {
                            AlertsService.alert({
                                button: {
                                    onClick: function(close) {
                                        document.location = './?cb=' + new Date().getTime();
                                    }
                                },
                                text: $translate.instant('impersonate-user.success.impersonatingUser', {
                                    userDisplayName: data.userDisplayName
                                }),
                                title: $translate.instant('global.messages.success.title')
                            });
                        },
                        function error() {
                            AlertsService.alert({
                                text: $translate.instant('impersonate-user.error.text'),
                                title: $translate.instant('global.messages.error.title')
                            });
                        }
                    );
                },

                init = function() {
                    AlertsServiceDelegate.buttons.enable('ok', false);
                    AlertsServiceDelegate.buttons.setOnClick('ok', impersonate);
                },

                onPasswordChange = function() {
                    AlertsServiceDelegate.buttons.enable('ok', !!data.password);
                },

                zend;

            init();

            return {
                data: data,
                onPasswordChange: onPasswordChange
            };
        }
    );
})();