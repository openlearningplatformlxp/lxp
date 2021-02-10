'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogController',
        function($scope, $location, $stateParams, CatalogService, CardService) {

        var data = {};
            var init = function() {

                var id = $stateParams.id;

                data.loading = true;
                CatalogService.getCatalogData({
                    id: id
                }, function(response) {
                    data.loading = false;
                    data.catalog = response;

                }, function(error) {
                    console.log(error);
                });
            };

            init();

            var getNumArray = function(num) {
                return new Array(num);
            };

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };

            var searchByTopic = function(topic) {
                $location.path('search/').search({
                    topic: topic
                });
            };

            return {
                data: data,
                getNumArray: getNumArray,
                searchByTopic: searchByTopic,
                notYetImplemented: notYetImplemented
            };
        }
    );

    module.factory('CatalogService', function($resource) {
        return $resource('api/catalog/type/:id', {}, {
            getCatalogData: {
                method: 'GET',
                params: {
                    id: "@id"
                }
            }
        });
    });

})();
