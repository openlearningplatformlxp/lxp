'use strict';

(function() {
    var module = angular.module('app.components');


    module.controller('TeamCourseManagerModalController',
        function($uibModalInstance, manager, type, TeamService, CourseService) {
            var data = {
                    managerStack: [],
                    isLoading: false
                },

                init = function() {
                    loadManagerData(manager);
                },

                loadManagerData = function(manager) {
                    data.isLoading = true;
                    data.manager = manager;
                    data.managers = [];
                    data.progressionOverview = null;
                    data.searchValue = null;

                    TeamService.getTeamChildData(manager.userId, type).then(function(response) {
                        prepareTeamProgressionData(response);
                        data.isLoading = false;
                    }, function(error) {
                        data.isLoading = false;
                        console.log(error);
                    });
                },

                prepareTeamProgressionData = function(teamData) {
                    var i,
                        iLen,
                        j,
                        jLen,
                        progressionStats,
                        memberStats,
                        teamMembers = teamData.teamMembers;

                    // TODO: (WJK) Replace with server-side data conversion? (send in correct format intially)

                    data.managers = [];
                    for (i = 0, iLen = teamMembers.length; i < iLen; i += 1) {
                        if (teamMembers[i].manager) {
                            data.managers.push(teamMembers[i]);
                        }
                    }

                    data.progressionOverview = teamData.progressionOverview;

                    for (i = 0, iLen = data.progressionOverview.length; i < iLen; i += 1) {
                        progressionStats = data.progressionOverview[i];

                        for (j = 0, jLen = progressionStats.teamMemberCompletionList.length; j < jLen; j += 1) {
                            memberStats = progressionStats.teamMemberCompletionList[j];
                            if (memberStats.percentComplete === 0) {
                                memberStats.status = "NOT_STARTED";
                            } else if (memberStats.percentComplete === 100) {
                                memberStats.status = "COMPLETE";
                            } else {
                                memberStats.status = "IN_PROGRESS";
                            }
                        }
                    }
                },

                close = function() {
                    $uibModalInstance.close(null);
                },

                teamProgressionFilter = function(teamProgression) {
                    // Check search
                    if (data.searchValue && data.searchValue.length && teamProgression.program.title.toUpperCase().indexOf(data.searchValue.toUpperCase()) === -1) {
                        return false;
                    }

                    if (type === 'LEARNING_PATH' && teamProgression.program.type === 'COURSE') {
                        return false;
                    }
                    return true;
                },

                goToManagerView = function(manager) {
                    data.managerStack.unshift({
                        manager: data.manager,
                        managers: data.managers,
                        progressionOverview: data.progressionOverview
                    });

                    loadManagerData(manager);
                },

                goToPreviousManagerView = function() {
                    var previousManagerData = data.managerStack.shift();
                    data.manager = previousManagerData.manager;
                    data.managers = previousManagerData.managers;
                    data.progressionOverview = previousManagerData.progressionOverview;
                    data.searchValue = null;
                },

                goToMemberView = function(member, program) {
                    data.member = member;
                    data.program = program;
                    data.programProgressionOverview = null;

                    if (program.type === "COURSE") {
                        data.displayType = "COURSE";
                        CourseService.getCourseProgressionOverview({
                            memberId: member.userId,
                            courseId: program.id
                        }).then(function(response) {
                            data.programProgressionOverview = response;
                        }, function(error) {
                            console.log(error);
                        });
                    } else {
                        data.displayType = "PROGRAM";
                        CourseService.getProgramProgressionOverview({
                            memberId: member.userId,
                            programId: program.id
                        }).then(function(response) {
                            data.programProgressionOverview = response;
                        }, function(error) {
                            console.log(error);
                        });
                    }
                },

                showManagerView = function() {
                    data.member = null;
                    data.program = null;
                    data.programProgressionOverview = null;
                },

                zend;

            init();

            return {
                data: data,
                close: close,
                teamProgressionFilter: teamProgressionFilter,
                goToManagerView: goToManagerView,
                goToPreviousManagerView: goToPreviousManagerView,
                goToMemberView: goToMemberView,
                showManagerView: showManagerView
            };
        }
    );
})();