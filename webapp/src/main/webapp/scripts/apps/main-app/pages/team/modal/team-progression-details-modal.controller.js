'use strict';

(function() {
    var module = angular.module('app.components');


    module.controller('TeamProgressionDetailsModalController',
        function($uibModalInstance, member, program, CourseService) {
            var data = {
                    member: member,
                    program: program,
                    isLoading: false
                },

                init = function() {
                    data.isLoading = true;
                    if (program.type.name === "COURSE") {
                        data.displayType = "COURSE";
                        CourseService.getCourseProgressionOverview({
                            memberId: member.userId,
                            courseId: program.id
                        }).then(function(response) {
                            data.progressionOverview = response;
                            data.isLoading = false;
                        }, function(error) {
                            console.log(error);
                            data.isLoading = false;
                        });
                    } else if (program.shared) {
                        data.displayType = "PROGRAM";
                        CourseService.getSharedProgramProgressionOverview({
                            memberId: member.userId,
                            programId: program.id
                        }).then(function(response) {
                            data.progressionOverview = response;
                            data.isLoading = false;
                        }, function(error) {
                            console.log(error);
                            data.isLoading = false;
                        });
                    } else {
                        data.displayType = "PROGRAM";
                        CourseService.getProgramProgressionOverview({
                            memberId: member.userId,
                            programId: program.id
                        }).then(function(response) {
                            data.progressionOverview = response;
                            data.isLoading = false;
                        }, function(error) {
                            console.log(error);
                            data.isLoading = false;
                        });
                    }
                },

                close = function() {
                    $uibModalInstance.close(null);
                },

                zend;

            init();

            return {
                data: data,
                close: close
            };
        }
    );
})();