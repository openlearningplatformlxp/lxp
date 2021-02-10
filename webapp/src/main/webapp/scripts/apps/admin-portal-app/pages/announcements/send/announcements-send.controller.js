/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminAnnouncementsSendController',
        function($http, $state, AlertsService, MessagesService) {
            var data = {
                    announcement: {
                        id: null,
                        message: null,
                        linkUrl: null,
                        linkText: null
                    }
                },
                pageMessages = MessagesService.getMessagesInstance(),
                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToAdminAnnouncements
                            }
                        });
                    } else {
                        goToAdminAnnouncements();
                    }
                },
                isFormDisabled = function(form) {
                    return form != null && (form.$invalid);
                },
                goToAdminAnnouncements = function() {
                    $state.go('announcements');
                },
                send = function(form) {

                    if (!isFormDisabled(form)) {

                        var postData = {
                                id: data.announcement.id,
                                message: data.announcement.message,
                                linkUrl: data.announcement.linkUrl,
                                linkText: data.announcement.linkText
                            },
                            url = 'api/admin/announcements/';


                        $http.post(url, postData, {}).then(
                            function success(response) {
                                pageMessages.addSuccess('Announcement has been sent.');

                                form.$setPristine();
                                form.$setUntouched();

                                data.announcement.id = null;
                                data.announcement.message = null;
                                data.announcement.linkUrl = null;
                                data.announcement.linkText = null;
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
