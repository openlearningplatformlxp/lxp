'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('TeamController',
        function($http, $location, $anchorScroll, TeamService, MessagingService) {

            var data = {},

                init = function() {
                    initPulling();
                },

                createDismissButton = function(notificationId) {
                    return [{
                        text: "Dismiss",
                        action: "api/message/dismiss/" + notificationId,
                        order: 0
                    }];
                },

                initPulling = function() {
                    setInterval(function() {
                        MessagingService.pullMessages({}).then(function success(response) {
                            data.notifications = response;

                            data.notifications.forEach(function(notification) {
                                notification.buttons = eval(notification.actionButtons) || createDismissButton(notification.id);
                            });

                        }, function(error) {
                            console.log(error);
                        });
                    }, 10000);
                },

                onButtonClick = function($index, button) {

                    $http.post(button.action).then(function(response) {
                        data.notifications.splice($index, 1);
                    }).catch(function(error) {
                        console.log(error);
                    });

                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },

                zend;

            init();

            return {
                data: data,
                onButtonClick: onButtonClick,
                notYetImplemented: notYetImplemented
            };
        }
    );

})();
