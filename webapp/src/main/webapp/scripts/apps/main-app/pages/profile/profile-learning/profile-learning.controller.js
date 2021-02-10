'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('ProfileLearningController',
        function($scope, moment, CourseService, CardService) {

            $scope.cardView = CardService.getView();
            $scope.sharedPathsCards = new Array();
            $scope.learningPathCards = new Array();
            $scope.archivedLearningPathCards = new Array();

            var init = function() {
                $scope.$on('onUpdatedCardView', function(event, data) {
                    $scope.cardView = data;
                });
                $scope.loading = true;
                CourseService.getPersonalPrograms().then(function(response) {
                    $scope.loading = false;
                    $scope.learningPathCards = new Array();
                    $scope.maxNumber = response.maxNumber;
                    $scope.totalCount = response.totalCount;
                    $scope.available = $scope.maxNumber - $scope.totalCount;
                    if ($scope.available < 0) {
                        $scope.available = 0;
                    }
                    $scope.sharedPaths = response.sharedWithMeItems;
                    $scope.learningPaths = response.programItems;
                    $scope.archivedLearningPaths = response.archivedProgramItems;

                    $scope.sharedPaths.forEach(function(path) {
                        $scope.sharedPathsCards.push(CardService.buildCard(path));
                    });

                    $scope.learningPaths.forEach(function(path) {
                        $scope.learningPathCards.push(CardService.buildCard(path));
                    });

                    $scope.archivedLearningPaths.forEach(function(path) {
                        $scope.archivedLearningPathCards.push(CardService.buildCard(path));
                    });
                }, function(error) {
                    console.log(error);
                    $scope.loading = false;
                });
            };

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };

            init();


            return {
                notYetImplemented: notYetImplemented
            };
        }
    );

})();
