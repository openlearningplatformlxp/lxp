'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('FeedbackActivityController',
        function($scope, AlertsService, MessagesService, $http, $rootScope, $translate) {

            var data = {
                    activity: null,
                    isSubmitting: false,
                    submitComplete: false
                },

                init = function() {
                    data.activity = $scope.activityData;
                    processIncomingData();
                },

                selectMultiOption = function(questionIndex, answerIndex) {

                    var idx = data.activity.content.questions[questionIndex].answer.indexOf(answerIndex);

                    // is currently selected
                    if (idx > -1) {
                        data.activity.content.questions[questionIndex].answer.splice(idx, 1);
                    }

                    // is newly selected
                    else {
                        data.activity.content.questions[questionIndex].answer.push(answerIndex);
                    }

                },

                processIncomingData = function() {
                    var i,
                        len;

                    for (i = 0, len = data.activity.content.questions.length; i < len; i++) {
                        if (data.activity.content.questions[i].subtype === 'check') {
                            data.activity.content.questions[i].answer = [];
                        }
                    }
                },

                submit = function() {
                    var config,
                        feedback,
                        questions,
                        feedbackCompletionData,
                        dataOut,
                        x,
                        xlen,
                        y,
                        ylen;

                    if (!answersValid()) {
                        $scope.messages.addError($translate.instant('lesson.feedback.submit-alert'));
                        return;
                    }

                    config = {
                        activity: $scope.activity,
                        success: submitSuccess,
                        fail: submitFailure
                    };

                    //process data before submitting
                    feedback = [];
                    questions = angular.copy(data.activity.content.questions);
                    for (x = 0, xlen = questions.length; x < xlen; x++) {
                        if (questions[x].type === 'label') {
                            continue;
                        }
                        if (questions[x].subtype === 'checkbox' || questions[x].type === 'multichoice') {
                            var answers = [];
                            for (y = 0, ylen = questions[x].answer.length; y < ylen; y++) {
                                answers.push({
                                    answer: questions[x].answer[y]
                                });
                            }
                            feedback.push({
                                id: questions[x].id,
                                answers: answers
                            });
                        } else {
                            feedback.push({
                                id: questions[x].id,
                                answer: questions[x].answer
                            });
                        }
                    }

                    feedbackCompletionData = {
                        cms: [{
                            moduleid: $scope.activityData.content.moduleId,
                            completion_status: 1,
                            feedback: feedback
                        }]
                    };

                    dataOut = {
                        moduleId: $scope.activityData.id,
                        data: JSON.stringify(feedbackCompletionData)
                    };

                    data.isSubmitting = true;

                    $http.post("api/courseplayer/activity/feedback/submit/" + $scope.activityData.courseId + "/" + $scope.activityData.id, dataOut)
                        .then(function(response) {
                            if (response.data.success) {
                                data.submitComplete = true;
                                AlertsService.alert({
                                    title: "Success",
                                    text: "Feedback as been successfully submitted."
                                });

                                $rootScope.$broadcast('activity.get-next');

                            } else {
                                data.isSubmitting = false;
                            }
                        }, function(error) {
                            data.isSubmitting = false;
                            submitFailure(error);
                        });
                },

                submitSuccess = function(response) {
                    if (response.errorcode) {
                        $scope.messages.addError($translate.instant('lesson.feedback.system-error') + response.message);
                        return;
                    }

                    AlertsService.alert({
                        title: $translate.instant('lesson.feedback.success-title'),
                        text: $translate.instant('lesson.feedback.success-text')
                    });
                },

                submitFailure = function(error) {
                    AlertsService.alert({
                        title: "Error submitting",
                        text: error.data.message
                    });
                },

                answersValid = function() {
                    var x,
                        len;
                    for (x = 0, len = data.activity.content.questions.length; x < len; x++) {
                        if (data.activity.content.questions[x].required === 1) {
                            if (!data.activity.content.questions[x].answer || !data.activity.content.questions[x].answer.length) {
                                return false;
                            }
                        }
                    }
                    return true;
                },

                zend;

            init();

            return {
                data: data,
                submit: submit,
                selectMultiOption: selectMultiOption
            }
        }
    );

    module.filter('feedbackratedfilter', function() {

        // In the return function, we must pass in a single parameter which will be the data we will work on.
        // We have the ability to support multiple other parameters that can be passed into the filter optionally
        return function(input) {

            var startValue = input.indexOf("####"),
                inputValue,
                rateValue,
                output;

            if (startValue !== -1) {
                inputValue = angular.copy(input);
                rateValue = input.substring(0, startValue);

                //strip out value
                output = inputValue.substring(rateValue.length + 4, inputValue.length);
                output = "(" + rateValue + ") " + output;
                return output;
            }

            return input;
        }
    });

})();
