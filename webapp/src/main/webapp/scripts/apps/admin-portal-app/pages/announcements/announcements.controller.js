/**
 * Admin Announcements Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminAnnouncementController',
        function(resolvedPagedSearch, $state, MessagesService, $http) {
            var searchbarOptions = {
                    textKeys: {
                        searchInputPlaceholder: 'announcements.searchAnnouncements.inputPlaceholder',
                        totalItemsCount: 'announcements.searchAnnouncements.totalAnnouncementsCount'
                    }
                },
                pageMessages = MessagesService.getMessagesInstance(),

                deleteAnnouncement = function(id) {
                    var url = 'api/admin/announcements/' + id;

                    $http.delete(url, {}).then(
                        function success(response) {
                            pageMessages.addSuccess('Announcement has been deleted.');
                            resolvedPagedSearch.reload();
                        },
                        function error(response) {
                            pageMessages.addHttpError(response);
                        }
                    );
                },

                goToSendAnnouncement = function() {
                    $state.go('announcements-send');
                };

            return {
                pagedSearch: resolvedPagedSearch,
                deleteAnnouncement: deleteAnnouncement,
                goToSendAnnouncement: goToSendAnnouncement,
                searchbarOptions: searchbarOptions
            };
        }
    );
})();
