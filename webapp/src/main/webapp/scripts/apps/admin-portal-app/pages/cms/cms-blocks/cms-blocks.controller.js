/**
 * Admin CMS Blocks Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminCmsBlocksController', function(resolvedPagedSearch, $state) {
        var searchbarOptions = {
                textKeys: {
                    searchInputPlaceholder: 'cms-blocks.searchUsers.inputPlaceholder',
                    totalItemsCount: 'cms-blocks.searchUsers.totalUsersCount'
                }
            },

            editCmsBlock = function(user, $event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                $state.go('cms-block-edit', {
                    cmsBlockId: user.id
                });
            },

            gotoNewUser = function() {
                $state.go('cms-block-add');
            };

        return {
            editCmsBlock: editCmsBlock,
            gotoNewUser: gotoNewUser,
            pagedSearch: resolvedPagedSearch,
            searchbarOptions: searchbarOptions
        };
    });
})();
