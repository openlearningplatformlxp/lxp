'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('QuizActivityController',
        function($scope, $sce, $timeout, $location, MessagesService, AlertsService, $http, $rootScope, CoursePlayerService) {

            var data = {
                    activity: $scope.activityData,
                    nextActivity: $scope.nextActivityData,
                    activityDescription: $scope.activityData.content.description,
                    showIntro: true,
                    showQuestions: false,
                    showResults: false,
                    quiz: $scope.activityData.content.quiz
                },

                init = function() {
                    data.totalQuestionCount = 0;
                    data.currentQuestionIndex = 0;
                    data.currentQuestion = {};

                    if (data.quiz) {
                        if (data.quiz.questions) {
                            data.currentQuestion = data.quiz.questions[0];
                            data.totalQuestionCount = data.quiz.questions.length;
                            data.instructionText = determineInstructionText(data.currentQuestion.type);
                        }
                    }
                },

                attemptQuiz = function() {

                    $scope.triggerCompleteOnView();

                    data.showIntro = false;
                    data.showQuestions = true;
                },

                determineInstructionText = function(type) {
                    if (type !== 'essay' && type !== 'shortanswer') {
                        return "Choose the correct answer below";
                    }
                    return null;
                },

                getLetter = function(num) {
                    return "&#" + (65 + num);
                },

                selectChoice = function(question, answer) {
                    var y,
                        yLen,
                        quizAnswer;

                    for (y = 0, yLen = question.answers.length; y < yLen; y++) {
                        quizAnswer = question.answers[y];
                        quizAnswer.selected = (quizAnswer.answerId === answer.answerId);
                    }
                    question.selectedAnswer = answer;
                },

                goToNextQuestion = function() {
                    if (data.currentQuestionIndex < data.quiz.questions.length - 1) {
                        data.currentQuestionIndex += 1;
                        data.currentQuestion = data.quiz.questions[data.currentQuestionIndex];
                        data.instructionText = determineInstructionText(data.currentQuestion.type);
                    }
                },

                submitQuiz = function() {
                    var x,
                        xLen,
                        y,
                        yLen;

                    //check if all answer selected
                    for (x = 0, xLen = data.quiz.questions.length; x < xLen; x++) {
                        if (!isQuestionAnswered(data.quiz.questions[x])) {
                            AlertsService.alert({
                                title: "Error",
                                text: "Please answer all questions before submitting."
                            });
                            return;
                        }
                    }

                    //process questions for submission
                    var questionDataOut = [];
                    for (var z = 0; z < data.quiz.questions.length; z++) {
                        var questionData = {
                            slot: data.quiz.questions[z].slot,
                            sequence_check: data.quiz.questions[z].sequenceCheck,
                            type: data.quiz.questions[z].type
                        };

                        switch (data.quiz.questions[z].type) {
                            case "multichoice":
                                for (var y = 0; y < data.quiz.questions[z].answers.length; y++) {
                                    if (data.quiz.questions[z].answers[y].selected) {
                                        questionData.answer_index = y;
                                        break;
                                    }
                                }
                                break;
                            case "multianswer":
                                questionData.answer_indexes = [];
                                for (var y = 0; y < data.quiz.questions[z].answers.length; y++) {
                                    if (data.quiz.questions[z].answers[y].selected) {
                                        questionData.answer_indexes.push(1);
                                    } else {
                                        questionData.answer_indexes.push(0);
                                    }
                                }
                                break;
                            case "truefalse":
                                questionData.answer_truefalse = angular.copy(data.quiz.questions[z].answer_truefalse);
                                break;
                            case "shortanswer":
                                questionData.answer_text = angular.copy(data.quiz.questions[z].answer_shortanswer);
                                break;
                            case "match":
                                var stemsAndChoices = [];
                                for (var a = 0; a < data.quiz.questions[z].matchingStems.length; a++) {
                                    stemsAndChoices.push({
                                        stem: data.quiz.questions[z].matchingStems[a].value,
                                        choiceIndex: data.quiz.questions[z].matchingStems[a].choice
                                    });
                                }
                                questionData.answer_match = stemsAndChoices;
                                break;
                            default:
                                console.log("ERROR: Unsupported Question Type: " + data.quiz.questions[z].type);
                                break;
                        }
                        questionDataOut.push(questionData);
                    }

                    var dataOut = {
                        attemptId: data.quiz.attemptId,
                        questions: questionDataOut
                    }

                    CoursePlayerService.submitQuizActivity({
                        courseId: data.quiz.courseId,
                        activityId: data.quiz.moduleId,
                        data: dataOut
                    }).then(function success(response) {
                        // TODO: (WJK) Check return data to verify actual success
                        // TODO: (WJK) Check return data to verify course completion????
                        data.showIntro = false;
                        data.showQuestions = false;
                        data.showResults = true;
                        data.resultsDescription = "Congratulations!!  Your quiz has been completed.";

                        AlertsService.alert({
                            title: "Passed",
                            text: "You have successfully passed the quiz."
                        });
                    }, function(error) {
                        console.log(error);
                    });
                },

                selectMultiChoice = function(quesiton, answer) {
                    for (var x = 0; x < data.quiz.questions.length; x++) {
                        if (data.quiz.questions[x].questionId == quesiton.questionId) {
                            for (var y = 0; y < data.quiz.questions[x].answers.length; y++) {
                                if (data.quiz.questions[x].answers[y].answerId == answer.answerId) {
                                    data.quiz.questions[x].answers[y].selected = !data.quiz.questions[x].answers[y].selected;
                                }
                            }
                            break;
                        }
                    }
                },

                selectSingleChoice = function(question, answer) {
                    for (var x = 0; x < data.quiz.questions.length; x++) {
                        if (data.quiz.questions[x].questionId == question.questionId) {
                            for (var y = 0; y < data.quiz.questions[x].answers.length; y++) {
                                if (data.quiz.questions[x].answers[y].answerId == answer.answerId) {
                                    data.quiz.questions[x].answers[y].selected = true;
                                } else {
                                    data.quiz.questions[x].answers[y].selected = false;
                                }
                            }
                            break;
                        }
                    }
                },

                isQuestionAnswered = function(questionIn) {
                    var questionAnswered = false;


                    //multianswer check  AND multichoice check
                    if (questionIn.type == "multianswer" || questionIn.type == "multichoice") {
                        for (var y = 0; y < questionIn.answers.length; y++) {
                            if (questionIn.answers[y].selected) {
                                questionAnswered = true;
                            }
                        }
                    }

                    //shortanswer check
                    if (questionIn.type == "shortanswer") {
                        if (questionIn.answer_shortanswer && questionIn.answer_shortanswer != "") {
                            questionAnswered = true;
                        }
                    }

                    //essay check
                    if (questionIn.type == "essay") {
                        if (questionIn.answer_essay && questionIn.answer_essay != "") {
                            questionAnswered = true;
                        }
                    }

                    //match check
                    if (questionIn.type == "match") {
                        var allValueStemsHaveChoices = true;
                        for (var z = 0; z < questionIn.matchingStems.length; z++) {
                            if (!questionIn.matchingStems[z].choice || questionIn.matchingStems[z].choice == "") {
                                allValueStemsHaveChoices = false;
                            }
                        }

                        if (allValueStemsHaveChoices) {
                            questionAnswered = true;
                        }
                    }

                    //truefalse check
                    if (questionIn.type == "truefalse") {
                        if (questionIn.answer_truefalse && questionIn.answer_truefalse != "") {
                            questionAnswered = true;
                        }
                    }

                    return questionAnswered;

                },

                zend;

            init();

            return {
                data: data,
                attemptQuiz: attemptQuiz,
                getLetter: getLetter,
                selectChoice: selectChoice,
                submitQuiz: submitQuiz,
                goToNextQuestion: goToNextQuestion,
                goToNextActivity: $scope.goToNextActivity,
                selectMultiChoice: selectMultiChoice,
                selectSingleChoice: selectSingleChoice,
                isQuestionAnswered: isQuestionAnswered
            };
        }
    );
})();
