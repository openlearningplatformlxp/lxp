'use strict';

(function() {
    var module = angular.module('app.components');


    module.controller('TeamIndividualManagerModalController',
        function($uibModalInstance, manager, TeamService, CourseService) {
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
                    data.teamMembers = null;
                    data.searchValue = null;

                    TeamService.getTeamChildData(manager.userId).then(function(response) {
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
                        teamMember,
                        progressionStats,
                        teamMemberStats,
                        progressionHash = {};

                    // TODO: (WJK) Replace with server-side data conversion? (send in correct format intially)

                    data.teamMembers = teamData.teamMembers;

                    for (i = 0, iLen = teamData.progressionOverview.length; i < iLen; i += 1) {
                        progressionStats = teamData.progressionOverview[i];
                        if (progressionStats.teamMemberCompletionList) {
                            for (j = 0, jLen = progressionStats.teamMemberCompletionList.length; j < jLen; j += 1) {
                                teamMemberStats = progressionStats.teamMemberCompletionList[j];
                                if (!progressionHash[teamMemberStats.teamMember.userId]) {
                                    progressionHash[teamMemberStats.teamMember.userId] = [];
                                }
                                progressionHash[teamMemberStats.teamMember.userId].push({
                                    program: progressionStats.program,
                                    percentComplete: teamMemberStats.percentComplete,
                                    teamMemberCompletionList: [] // TODO: (WJK) REMOVE
                                })
                            }
                        }
                    }

                    for (i = 0, iLen = data.teamMembers.length; i < iLen; i += 1) {
                        teamMember = data.teamMembers[i];
                        teamMember.progressionOverview = progressionHash[teamMember.userId] || [];
                        teamMember.searchValue = teamMember.firstName.toUpperCase() + " " + teamMember.lastName.toUpperCase() // For search efficiency
                    }
                },

                close = function() {
                    $uibModalInstance.close(null);
                },

                teamMemberFilter = function(teamMember) {
                    // Check search
                    if (data.searchValue && data.searchValue.length && teamMember.searchValue.indexOf(data.searchValue.toUpperCase()) === -1) {
                        return false;
                    }

                    return true;
                },

                goToManagerView = function(manager) {
                    data.managerStack.unshift({
                        manager: data.manager,
                        teamMembers: data.teamMembers,
                        searchValue: data.searchValue
                    });

                    loadManagerData(manager);
                },

                goToPreviousManagerView = function() {
                    var previousManagerData = data.managerStack.shift();
                    data.manager = previousManagerData.manager;
                    data.teamMembers = previousManagerData.teamMembers;
                    data.searchValue = previousManagerData.searchValue;
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
                teamMemberFilter: teamMemberFilter,
                goToManagerView: goToManagerView,
                goToPreviousManagerView: goToPreviousManagerView,
                goToMemberView: goToMemberView,
                showManagerView: showManagerView
            };
        }
    );
})();