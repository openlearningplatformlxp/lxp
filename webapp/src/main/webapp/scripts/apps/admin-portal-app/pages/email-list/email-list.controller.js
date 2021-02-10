/**
 * Admin Mail List Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminEmailListController',
        function(resolvedPagedSearch, $rootScope, ViewEmailModal) {
            var searchbarOptions = {
                    textKeys: {
                        searchInputPlaceholder: 'email-list.searchEmail.inputPlaceholder',
                        totalItemsCount: 'email-list.searchEmail.totalEmailCount'
                    }
                },

                getLastActivityDate = function(email) {
                    if (email.sentDate) {
                        return email.sentDate;
                    } else if (email.lastAttemptDate) {
                        return email.lastAttemptDate;
                    } else {
                        return email.lastModifiedDate;
                    }
                },

                init = function() {
                    $rootScope.$on('email.list.add', function(event, email) {
                        resolvedPagedSearch.addEntry(email);
                    });
                },

                isStatusFailed = function(email) {
                    return (!isStatusSent(email) && email.attemptCount == 10);
                },

                isStatusPending = function(email) {
                    return (!isStatusSent(email) && !isStatusFailed(email));
                },

                isStatusSent = function(email) {
                    return (!!email.sentDate);
                },

                viewEmail = function(email, $event) {
                    if ($event) {
                        $event.preventDefault();
                        $event.stopPropagation();
                    }

                    ViewEmailModal.showEmail({
                        email: email
                    });
                },

                zend;

            init();

            return {
                pagedSearch: resolvedPagedSearch,
                searchbarOptions: searchbarOptions,

                getLastActivityDate: getLastActivityDate,
                isStatusFailed: isStatusFailed,
                isStatusPending: isStatusPending,
                isStatusSent: isStatusSent,
                viewEmail: viewEmail
            };
        }
    );
})();
