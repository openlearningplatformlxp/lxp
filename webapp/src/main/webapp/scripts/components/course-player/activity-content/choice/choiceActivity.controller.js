'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('ChoiceActivityController', function($scope, $sce, $http, $rootScope, AlertsService, $translate) {

        var data = {
            isDisabled: true,
            content: {},
            choice: {},
            activityData: {}
        };

        var singleChoiceIsSelected = function(optionid) {
            data.isDisabled = false;

            angular.forEach(data.choice.options, function(option) {
                if (option.optionid != optionid) {
                    if (option.selected) {
                        option.selected = false;
                    }
                }
            });
        };

        var multipleChoicesAreSelected = function() {
            data.isDisabled = false;
        };

        var processData = function(run) {
            if (data.content.status == 'COMPLETE') {
                for (var i = 0; i < data.choice.options.length; i++) {
                    data.choice.options[i].percentage = Math.round(data.choice.options[i].number / data.choice.numberofanswers * 100);
                }
            }
        };

        var getPercentage = function(percentage) {
            return percentage + "%";
        };

        var submit = function() {

            data.isDisabled = true;

            $http.post("api/courseplayer/choice/submit/" + data.activityData.id, data.choice).then(function(response) {
                data.choice = response.data;
                $rootScope.$broadcast('activity.refresh-status');
                data.content.status = 'COMPLETE';
                data.isDisabled = false;
                processData(true);

            }, function(error) {
                data.isDisabled = false;

                AlertsService.alert({
                    title: $translate.instant('lesson.choice.error-title'),
                    text: $translate.instant('lesson.choice.error-text')
                });

            });
        };

        var init = function() {
            data.activityData = $scope.activityData;

            $http.get("api/courseplayer/choice/get/" + data.activityData.id).then(function(response) {
                data.choice = response.data;
                data.isDisabled = true;
                processData(true);
            }, function(error) {

            });
        };

        init();

        $scope.ctrl = {
            data: data,
            singleChoiceIsSelected: singleChoiceIsSelected,
            multipleChoicesAreSelected: multipleChoicesAreSelected,
            processData: processData,
            getPercentage: getPercentage,
            submit: submit
        };
    });
})();
