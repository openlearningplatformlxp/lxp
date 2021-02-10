/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminWikipageTreeController',
        function($http, $state, $scope, $stateParams, AlertsService, MessagesService, WikipageTreeService) {
            var data = {
                    models: {
                        selected: null,
                        templates: [{
                            type: "folder",
                            id: 1,
                            slug: '/subfolder',
                            columns: []
                        }],
                        dropzones: {
                            "/pages": []
                        }
                    }
                },
                pageMessages = MessagesService.getMessagesInstance(),
                updateTree = function() {

                    var url = 'api/admin/wikipages/tree',
                        postData = {
                            id: -1,
                            type: "folder",
                            slug: "/pages",
                            columns: data.models.dropzones["/pages"]
                        };
                    var request = $http.put(url, postData, {});

                    request.then(
                        function success(response) {
                            pageMessages.addSuccess('Wikipage Folder Tree has been updated!');
                        },
                        function(error) {
                            pageMessages.addError('Wikipage Folder Tree failed to be updated!');
                        }
                    );

                },
                init = function() {
                    WikipageTreeService.getTree(function(response) {
                        data.models.dropzones["/pages"] = response.columns;
                    }, function(error) {
                        console.log(error);
                    })
                };

            init();

            return {
                data: data,
                updateTree: updateTree,
                pageMessages: pageMessages
            };
        }
    );

    module.factory('WikipageTreeService', function($resource) {
        return $resource('api/admin/wikipages/tree/', {}, {
            getTree: {
                method: 'GET'
            }
        });
    });
})();
