'use strict';

(function() {
    var module = angular.module('app.components');


    module.controller('LastQuestionModalController',
        function($uibModalInstance, unansweredQuestions) {
            var data = {
                    unansweredQuestions: unansweredQuestions
                },

                close = function() {
                    $uibModalInstance.close(null);
                },

                jumpToQuestion = function(questionId) {
                    $uibModalInstance.close(questionId);
                },

                jumpToFirstUnansweredQuestion = function() {
                    $uibModalInstance.close(unansweredQuestions[0].id);
                };

            return {
                data: data,
                close: close,
                jumpToQuestion: jumpToQuestion,
                jumpToFirstUnansweredQuestion: jumpToFirstUnansweredQuestion
            };
        }
    );
})();