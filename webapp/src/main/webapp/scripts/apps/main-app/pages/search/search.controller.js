'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('SearchController',
        function($rootScope, $scope, $location, SearchService, TagsService, CardService, $stateParams, Principal, localStorageService) {

            $scope.cardView = CardService.getView();
            $scope.page = 0;
            $scope.size = 10;

            $scope.filterTags = {
                skillLevels: null,
                languages: null
            };

            var storeSearchFilters = true,

                init = function() {
                    var searchTopic = $location.search()['topic'],
                        searchType = $location.search()['type'],
                        searchStatus = $location.search()['status'],
                        searchRole = $location.search()['role'],
                        searchRoleName = $location.search()['roleName'],
                        searchContent = $location.search()['content'],
                        searchDelivery = $location.search()['delivery'],
                        searchSkill = $location.search()['skill'],
                        searchLanguage = $location.search()['language'],
                        searchCountry = $location.search()['country'],
                        searchCity = $location.search()['city'],
                        storedFilters = localStorageService.get('filters');

                    $scope.$on('onUpdatedCardView', function(event, data) {
                        $scope.cardView = data;
                    });

                    if (!searchTopic &&
                        !searchType &&
                        !searchStatus &&
                        !searchRole &&
                        !searchContent &&
                        !searchDelivery &&
                        !searchSkill &&
                        !searchLanguage &&
                        !searchCountry &&
                        !searchCity) {

                        $scope.filters = storedFilters || {};

                    } else {
                        storeSearchFilters = false;

                        $scope.filters = {
                            role: searchRole,
                            roleName: searchRoleName,
                            content: searchContent,
                            topic: searchTopic,
                            type: searchType !== null && searchType !== undefined ? [searchType] : null,
                            status: searchStatus
                        }
                    }
                    findTags();
                    refresh();

                    if (Principal.isAuthenticated()) {
                        Principal.hasAuthority('ROLE_CMS_EDITOR').then(function(hasAuthority) {
                            $scope.showOpenInLms = hasAuthority;
                        })
                    }
                },
                clearAllFilters = function() {
                    $scope.filters.status = null;
                    $scope.filters.topic = null;
                    $scope.filters.role = null;
                    $scope.filters.type = [];
                    $scope.filters.content = [];
                    $scope.filters.delivery = [];
                    $scope.filters.skill = null;
                    $scope.filters.language = null;
                    $scope.filters.country = null;
                    $scope.filters.city = null;
                    refresh();
                },
                clearFilter = function(filter) {
                    // clear query parameter
                    var params = $location.search() || {};
                    params[filter] = null;
                    $location.search(params);

                    $scope.filters[filter] = null;
                    refresh();
                },
                clearArrayFilter = function(filter, value) {
                    // clear query parameter
                    var params = $location.search() || {};
                    if (params[filter] != null && params[filter].indexOf(value) >= 0) {
                        if (Array.isArray(params[filter])) {
                            params[filter].splice(params[filter].indexOf(value), 1);
                        } else {
                            params[filter] = null;
                        }
                    }
                    $location.search(params);
                    if ($scope.filters[filter] != null && $scope.filters[filter].indexOf(value) >= 0) {
                        $scope.filters[filter].splice($scope.filters[filter].indexOf(value), 1);
                    }
                    refresh();
                },
                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },
                findTags = function() {
                    TagsService.findTags({}, function(response) {
                        $scope.filterTags = response;
                    }, function(error) {
                        console.log(error);
                    });
                },
                loadMore = function() {
                    if ($scope.search.content != null && $scope.search.totalPages > $scope.page + 1) {
                        $scope.page++;
                        search();
                    }
                },
                isValid = function(param) {
                    var valid = param !== null && param !== undefined;
                    if (valid && Array.isArray(param) && param.length > 0) {
                        valid = param[0] !== null && param[0] !== undefined;
                    }
                    return valid;
                },
                search = function() {

                    var q = $stateParams.q;

                    var status = isValid($scope.filters.status) ? $scope.filters.status : null;
                    var topic = isValid($scope.filters.topic) ? $scope.filters.topic : null;
                    var role = isValid($scope.filters.role) ? $scope.filters.role : null;
                    var cms = isValid($scope.filters.content) ? $scope.filters.content : null;
                    var delivery = isValid($scope.filters.delivery) ? $scope.filters.delivery.id : null;
                    var skillLevel = isValid($scope.filters.skill) ? $scope.filters.skill.name : null;
                    var language = isValid($scope.filters.language) ? $scope.filters.language.name : null;
                    var country = isValid($scope.filters.country) ? $scope.filters.country : null;
                    var city = isValid($scope.filters.city) ? $scope.filters.city : null;
                    var type = isValid($scope.filters.type) ? $scope.filters.type : null;

                    $scope.loading = true;
                    $rootScope.$broadcast('onUpdatedSearch', q);
                    SearchService.searchValues({
                        value: q,
                        type: type,
                        status: status,
                        topic: topic,
                        role: role,
                        cms: cms,
                        delivery: delivery,
                        skillLevel: skillLevel,
                        language: language,
                        country: country,
                        city: city,
                        page: $scope.page,
                        size: $scope.size
                    }, function(response) {
                        $scope.firstLoading = false;
                        $scope.loading = false;
                        $scope.search = response.items;
                        $scope.total = response.items.totalElements;

                        if (response.featuredItems == null) {
                            response.featuredItems = [];
                        }

                        response.featuredItems.forEach(function(path) {
                            $scope.featuredCards.push(CardService.buildCard(path));
                        });

                        $scope.search.content.forEach(function(path) {
                            $scope.itemCards.push(CardService.buildCard(path));
                        });

                    }, function(error) {
                        console.log(error);
                    });
                },
                refresh = function() {
                    if (storeSearchFilters) {
                        localStorageService.set('filters', $scope.filters);
                    }
                    $scope.firstLoading = true;
                    $scope.page = 0;
                    $scope.total = 0;
                    $scope.itemCards = new Array();
                    $scope.featuredCards = new Array();
                    search();
                }

            init();

            return {
                clearFilter: clearFilter,
                clearAllFilters: clearAllFilters,
                clearArrayFilter: clearArrayFilter,
                loadMore: loadMore,
                refresh: refresh,
                notYetImplemented: notYetImplemented
            };
        }
    );

    module.factory('SearchService', function($resource) {
        return $resource('api/search/:value', {}, {
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

    module.factory('TagsService', function($resource) {
        return $resource('api/search/tags', {}, {
            findTags: {
                method: 'GET'
            }
        });
    });


})();
