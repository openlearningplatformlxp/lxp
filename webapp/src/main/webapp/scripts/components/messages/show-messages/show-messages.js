/**
 * Show Messages
 *
 * Example:
 *
 *     In a controller:
 *
 *         See file: messages.service.js
 *
 *     In a template:
 *
 *         <show-messages messages="ctrl.messages"></show-messages>
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('showMessages',
        function() {
            return {
                bindToController: {
                    options: '=',
                    messages: '='
                },
                controller: 'ShowMessagesController',
                controllerAs: 'ctrl',
                restrict: 'E',
                scope: {},
                templateUrl: 'scripts/components/messages/show-messages/show-messages.html'
            }
        }
    );

    module.controller('ShowMessagesController',
        function($anchorScroll) {
            var messages = this.messages,
                opts = angular.copy(this.options) || {},

                dismissable = true,
                showMessagesId = 'showMessagesId' + new Date().getTime(),

                init = function() {
                    if (angular.isDefined(opts.dismissable)) {
                        dismissable = !!opts.dismissable;
                    }

                    messages.setAddMessageCallback(function() {
                        scrollIntoView();
                    });
                },

                isDismissable = function() {
                    return dismissable;
                },

                scrollIntoView = function() {
                    var origOffset = $anchorScroll.yOffset;

                    $anchorScroll.yOffset = 10;
                    $anchorScroll(showMessagesId);
                    $anchorScroll.yOffset = origOffset;
                };

            init();

            return {
                messages: messages,

                isDismissable: isDismissable,
                showMessagesId: showMessagesId
            };
        }
    );
})();
