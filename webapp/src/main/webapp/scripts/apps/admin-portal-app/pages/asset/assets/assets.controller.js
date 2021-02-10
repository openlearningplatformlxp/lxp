/**
 * Admin Assets Controller
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminAssetsController', function(resolvedPagedSearch, $state) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'asset.assets.searchUsers.inputPlaceholder',
                    totalItemsCount: 'asset.assets.searchUsers.totalUsersCount'
                }
            },

            editAsset = function(user, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('asset-edit', {
                    assetId: user.id
                });
            },

            gotoNewUser = function() {
                $state.go('asset-add');
            };

        return {
            editAsset: editAsset,
            gotoNewUser: gotoNewUser,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions
        };
    });
})();
