/**
 * Admin Notifications Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminFeaturedSearchCreateController',
        function($http, $state, $scope, $stateParams, AlertsService, MessagesService, AdminSearchService, CardService, resolvedData) {

            var data = {
                    cardView: CardService.getView(),
                    page: 0,
                    size: 10,
                    featuredSearch: {
                        featuredSearchId: null,
                        keywords: null,
                        instances: []
                    }
                },
                pageMessages = MessagesService.getMessagesInstance(),
                cancel = function(form) {
                    if (form.$dirty) {
                        AlertsService.confirmCancel({
                            buttonOk: {
                                onClick: goToFeaturedSearches
                            }
                        });
                    } else {
                        goToFeaturedSearches();
                    }
                },
                isFormDisabled = function(form) {
                    return (!data.featuredSearch.instances || data.featuredSearch.instances.length === 0) || form != null && form.$invalid;
                },
                goToFeaturedSearches = function() {
                    $state.go('featured-searches');
                },
                send = function(form) {

                    if (!isFormDisabled(form)) {
                        var postData = {
                                featuredSearchId: data.featuredSearch.featuredSearchId,
                                keywords: data.featuredSearch.keywords,
                                instances: data.featuredSearch.instances,
                            },
                            url = 'api/admin/featured-searches/';

                        var request = null;
                        if (data.featuredSearch.featuredSearchId != null) {
                            request = $http.put(url, postData, {});
                        } else {
                            request = $http.post(url, postData, {});
                        }

                        request.then(
                            function success(response) {
                                if (data.featuredSearch.featuredSearchId != null) {
                                    pageMessages.addSuccess('Featured search has been updated!');
                                } else {
                                    pageMessages.addSuccess('Featured search has been saved!');
                                }
                                data.featuredSearch.featuredSearchId = response.data.featuredSearchId;
                                form.$setPristine();
                                form.$setUntouched();
                            },
                            function error(response) {
                                pageMessages.addHttpError(response);
                            }
                        );
                    }
                },
                init = function() {
                    $scope.$on('onUpdatedCardView', function(event, d) {
                        data.cardView = d;
                    });
                    if ($stateParams.id != null) {
                        data.featuredSearch = resolvedData.featuredSearch;
                    }
                    refresh();
                },
                loadMore = function() {
                    if (data.search != null && data.search.content != null && data.search.content.length > 0) {
                        data.page++;
                        search();
                    }
                },
                search = function() {

                    var q = data.featuredSearch.searchText;

                    if (data.featuredSearch.searchText != null && data.featuredSearch.searchText.length >= 1) {
                        data.loading = true;
                        AdminSearchService.searchValues({
                            value: q,
                            cms: 'LMS',
                            page: data.page,
                            size: data.size
                        }, function(response) {
                            data.firstLoading = false;
                            data.loading = false;
                            data.search = response.items;
                            data.total = response.items.totalElements;

                            response.featuredItems.forEach(function(path) {
                                var card = CardService.buildCard(path);
                                card.selected = isSelected(card);
                                data.itemCards.push(card);
                            });

                            data.search.content.forEach(function(path) {
                                var card = CardService.buildCard(path);
                                card.selected = isSelected(card);
                                data.itemCards.push(card);
                            });

                        }, function(error) {
                            console.log(error);
                        });
                    } else {
                        data.firstLoading = false;
                        data.loading = false;
                        data.search = {};
                        data.total = 0;
                        data.itemCards = [];
                    }
                },
                refresh = function() {
                    data.firstLoading = true;
                    data.page = 0;
                    data.total = 0;
                    data.itemCards = new Array();
                    search();
                },
                removeSelection = function(instance) {
                    data.featuredSearch.instances = data.featuredSearch.instances.filter(function(i) {
                        return i.instanceId !== instance.instanceId;
                    });
                    data.itemCards.forEach(function(c) {
                        if (c.id === instance.instanceId) {
                            c.selected = false;
                        }
                    });
                },
                onSelected = function(card) {
                    if (!isSelected(card)) {
                        data.featuredSearch.instances.push({
                            instanceId: card.id,
                            instanceType: card.type,
                            instanceName: card.title
                        })
                    } else {
                        data.featuredSearch.instances = data.featuredSearch.instances.filter(function(i) {
                            return i.instanceId !== card.id;
                        })
                    }
                },
                isSelected = function(card) {
                    var selected = false;

                    data.featuredSearch.instances.forEach(function(i) {
                        if (i.instanceId === card.id) {
                            selected = true;
                        }
                    });

                    return selected;
                };

            init();

            return {
                data: data,
                pageMessages: pageMessages,
                send: send,
                loadMore: loadMore,
                refresh: refresh,
                isFormDisabled: isFormDisabled,
                cancel: cancel,
                removeSelection: removeSelection,
                onSelected: onSelected,
                isSelected: isSelected,
            };
        }
    );

    module.factory('AdminSearchService', function($resource) {
        return $resource('api/admin/search/:value', {}, {
            searchValues: {
                method: 'GET',
                params: {
                    value: "@value",
                    type: "@type",
                    skillLevel: "@skillLevel",
                    language: "@language"
                }
            }
        });
    });

})();
