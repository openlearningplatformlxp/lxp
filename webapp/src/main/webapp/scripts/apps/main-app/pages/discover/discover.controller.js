'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('DiscoverController',
        function($scope, $location, DiscoverService, CardService, Principal) {

            $scope.cardView = CardService.getView();

            var init = function() {
                $scope.returnPath = $location.url();
                Principal.identity().then(function(account) {
                    $scope.account = account;
                });
                $scope.$on('onLoggedIn', function(event, data) {
                    $scope.account = data.account;
                });
                $scope.$on('onUpdatedCardView', function(event, data) {
                    $scope.cardView = data;
                });

                $scope.loading = true;
                DiscoverService.getDiscoverData(function(response) {
                    $scope.loading = false;
                    $scope.discover = response;
                    $scope.nullContent = true;
                    $scope.discoverProgramsItems = [];

                    $scope.discover.discoverPrograms.forEach(function(path) {
                        var card = CardService.buildCard(path);

                        // Adding returnPath, since carousel will change scope for the card
                        if (card.discoveryProgramType == 'PROGRAM') {
                            card.returnPath = $scope.returnPath;
                        } else {
                            card.returnPath = null;
                        }

                        $scope.discoverProgramsItems.push(card);
                    });

                    // Show at least 1 card but no more than 2 at time
                    $scope.discoverProgramsNumSlidesToShow = Math.max(1, Math.min($scope.discoverProgramsItems.length - 1, 2));

                    $scope.discover.interests.forEach(function(interest) {
                        interest.programs.courses = [];
                        interest.programs.content.forEach(function(course) {
                            $scope.nullContent = false;
                            interest.programs.courses.push(CardService.buildCard(course));
                        });
                    });

                    $scope.discover.roles.forEach(function(interest) {
                        interest.programs.courses = [];
                        interest.programs.content.forEach(function(course) {
                            $scope.nullContent = false;
                            interest.programs.courses.push(CardService.buildCard(course));
                        });
                    });

                    if (Principal.isAuthenticated()) {
                        Principal.hasAuthority('ROLE_CMS_EDITOR').then(function(hasAuthority) {
                            $scope.showOpenInLms = hasAuthority;
                        })
                    }

                }, function(error) {
                    console.log(error);
                });
            };

            init();

            var searchByTopic = function(topic) {
                $location.path('search/').search({
                    topic: topic,
                    content: ['LMS']
                });
            };

            var searchByRole = function(tag) {
                // NOTE: (WJK) Revising to topic search since tags allow for regular search

                $location.path('search/').search({
                    topic: tag.name,
                    content: ['LMS']
                });
            };

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };

            return {
                notYetImplemented: notYetImplemented,
                searchByTopic: searchByTopic,
                searchByRole: searchByRole
            };
        }
    );

    module.factory('DiscoverService', function($resource) {
        return $resource('api/discover', {}, {
            getDiscoverData: {
                method: 'GET'
            }
        });
    });
})();
