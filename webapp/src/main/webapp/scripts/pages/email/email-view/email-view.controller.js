/**
 * Email View Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('ViewEmailController',
        function(resolvedEmail, $http, $rootScope, $translate, AlertsService, AlertsServiceDelegate, IframeResizerService) {
            var email = resolvedEmail,

                confirmResendEmail = function() {
                    AlertsService.confirm({
                        buttonOk: {
                            onClick: resendEmail
                        },
                        text: $translate.instant('email-view.resend.confirm.text'),
                        title: $translate.instant('email-view.resend.confirm.title')
                    });
                },

                iframeResizerHtml = IframeResizerService.getIframeResizerInstance({
                    content: email.htmlText
                }),

                init = function() {
                    if (!!email.sentDate) {
                        AlertsServiceDelegate.buttons.add({
                            class: 'btn-default',
                            onClick: function() {
                                confirmResendEmail();

                                return false;
                            },
                            position: 'left',
                            text: $translate.instant('email-view.resend.buttonText')
                        });
                    }
                },

                resendEmail = function() {
                    $http.get('api/admin/email/resend/' + email.id).then(
                        function success(response) {
                            AlertsService.alert({
                                text: $translate.instant('email-view.resend.sent.text'),
                                title: $translate.instant('email-view.resend.sent.title')
                            });

                            $rootScope.$broadcast('email.list.add', response.data);
                        },
                        function error() {
                            AlertsService.alert({
                                text: $translate.instant('email-view.resend.error.text'),
                                title: $translate.instant('global.messages.error.title')
                            });
                        }
                    );
                },

                zend;

            init();

            return {
                email: email,
                iframeResizerHtml: iframeResizerHtml
            };
        }
    );
})();
