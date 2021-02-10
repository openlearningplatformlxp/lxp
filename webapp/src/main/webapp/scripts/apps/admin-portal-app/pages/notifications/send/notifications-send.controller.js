/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminNotificationsSendController',
        function(resolvedData, $http, $state, AlertsService, MessagesService) {
            var data = {
                    programs: resolvedData.programs,
                    notification: {
                        title: null,
                        message: null,
                        showPrograms: false,
                        programId: null
                    }
                },
                pageMessages = MessagesService.getMessagesInstance(),
                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToAdminNotifications
                            }
                        });
                    } else {
                        goToAdminNotifications();
                    }
                },
                isFormDisabled = function(form) {
                    if (data.notification.showPrograms) {
                        if (!data.notification.programId) {
                            return true;
                        }
                    }

                    return form.$invalid || form.$pristine;
                },
                goToAdminNotifications = function() {
                    $state.go('notifications');
                },
                send = function(form) {

                    if (!isFormDisabled(form)) {
                        var programId = data.notification.showPrograms ? data.notification.programId : null;

                        var postData = {
                                title: data.notification.title,
                                message: data.notification.message,
                                programId: programId
                            },
                            url = 'api/admin/notifications/send';


                        $http.post(url, postData, {}).then(
                            function success(response) {
                                pageMessages.addSuccess('Message has been sent.');

                                form.$setPristine();
                                form.$setUntouched();

                                data.notification.title = null;
                                data.notification.message = null;
                                data.notification.showPrograms = false;
                                data.notification.programId = null;
                            },
                            function error(response) {
                                pageMessages.addHttpError(response);
                            }
                        );
                    }
                };

            return {
                data: data,
                pageMessages: pageMessages,
                isFormDisabled: isFormDisabled,
                cancel: cancel,
                send: send
            };
        }
    );
})();
