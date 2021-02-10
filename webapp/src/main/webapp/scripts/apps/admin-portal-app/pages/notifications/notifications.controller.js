/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminNotificationsController',
        function(resolvedPagedSearch, $state) {
            var searchbarOptions = {
                    textKeys: {
                        searchInputPlaceholder: 'notifications.searchNotifications.inputPlaceholder',
                        totalItemsCount: 'notifications.searchNotifications.totalNotificationsCount'
                    }
                },

                goToSendNotification = function() {
                    $state.go('notifications-send');
                };

            return {
                pagedSearch: resolvedPagedSearch,
                goToSendNotification: goToSendNotification,
                searchbarOptions: searchbarOptions
            };
        }
    );
})();
