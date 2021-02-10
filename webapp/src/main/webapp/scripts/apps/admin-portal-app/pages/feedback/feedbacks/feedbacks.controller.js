/**
 * Admin Assets Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminFeedbacksController', function(resolvedPagedSearch, $scope, $state) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'feedback.feedbacks.searchFeedbacks.inputPlaceholder',
                    totalItemsCount: 'feedback.feedbacks.searchFeedbacks.totalFeedbacksCount'
                }
            },
            setPopupFeedback = function(feedback) {
                $scope.popupFeedback = feedback;
            };

        return {
            setPopupFeedback: setPopupFeedback,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions
        };
    });
})();
