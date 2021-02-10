'use strict';

(function() {
    var module = angular.module('app.apps.main-app'),

        PROGRESSION_TYPES = [{
                name: "Learning Path",
                type: "LEARNING_PATH"
            },
            {
                name: "Course",
                type: "COURSE"
            }
        ];

    module.controller('TeamIndividualController',
        function($http, $location, $anchorScroll, $uibModal, TeamService) {

            var data = {
                    progressionTypes: PROGRESSION_TYPES,
                    progressionType: PROGRESSION_TYPES[0],
                    isLoading: false,
                    startDate: null,
                    endDate: null,
                    originalTeamMembers: null
                },

                init = function() {
                    data.isLoading = true;
                    data.startDate = null;
                    data.endDate = null;
                    TeamService.getTeamData(null).then(function(response) {
                        prepareTeamProgressionData(response);
                        data.isLoading = false;
                        data.originalTeamMembers = angular.copy(data.teamMembers);
                    }, function(error) {
                        data.isLoading = false;
                        console.log(error);
                    });
                },

                managerHasManagers = function() {
                    var man = false;
                    data.teamMembers.forEach(function(t) {
                        if (t.manager) {
                            man = true;
                        }
                    });
                    return man;
                },

                prepareTeamProgressionData = function(teamData) {
                    var i,
                        iLen,
                        j,
                        jLen,
                        teamMember,
                        progressionStatus,
                        teamMemberStatus,
                        progressionHash = {};

                    // TODO: (WJK) Replace with server-side data conversion? (send in correct format intially)

                    data.teamMembers = teamData.teamMembers;

                    for (i = 0, iLen = teamData.progressionOverview.length; i < iLen; i += 1) {
                        progressionStatus = teamData.progressionOverview[i];
                        if (progressionStatus.teamMemberCompletionList) {
                            for (j = 0, jLen = progressionStatus.teamMemberCompletionList.length; j < jLen; j += 1) {
                                teamMemberStatus = progressionStatus.teamMemberCompletionList[j];
                                if (!progressionHash[teamMemberStatus.teamMember.userId]) {
                                    progressionHash[teamMemberStatus.teamMember.userId] = [];
                                }
                                if (teamMemberStatus.percentComplete === 0) {
                                    teamMemberStatus.status = "NOT_STARTED";
                                } else if (teamMemberStatus.percentComplete === 100) {
                                    teamMemberStatus.status = "COMPLETE";
                                } else {
                                    teamMemberStatus.status = "IN_PROGRESS";
                                }
                                progressionHash[teamMemberStatus.teamMember.userId].push({
                                    program: progressionStatus.program,
                                    percentComplete: teamMemberStatus.percentComplete,
                                    timeEnrolled: teamMemberStatus.timeEnrolled,
                                    daysInProgress: teamMemberStatus.daysInProgress,
                                    timeCompleted: teamMemberStatus.completedDate,
                                    status: teamMemberStatus.status,
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

                teamMemberFilter = function(teamMember) {
                    // Check search
                    if (data.searchValue && data.searchValue.length && teamMember.searchValue.indexOf(data.searchValue) === -1) {
                        return false;
                    }

                    return true;
                },

                isManagerFilter = function(teamMember) {
                    return teamMember.manager;
                },
                isNotManagerFilter = function(teamMember) {
                    return !teamMember.manager;
                },

                search = function(searchValue) {
                    data.searchValue = searchValue ? searchValue.toUpperCase() : null;
                },

                setStartDate = function(startDate) {
                    data.isLoading = true;
                    data.startDate = startDate;

                    setDateRange();
                    data.isLoading = false;
                },

                setEndDate = function(endDate) {
                    data.isLoading = true;
                    data.endDate = endDate;

                    setDateRange();
                    data.isLoading = false;
                },

                setDateRange = function() {
                    data.teamMembers = angular.copy(data.originalTeamMembers);

                    if (!data.startDate && !data.endDate) {
                        return;
                    }

                    if (data.teamMembers && data.teamMembers.length > 0) {
                        var i = data.teamMembers.length;
                        while (i--) {
                            if (data.teamMembers[i].progressionOverview && data.teamMembers[i].progressionOverview.length > 0) {
                                var j = data.teamMembers[i].progressionOverview.length;
                                while (j--) {
                                    if (data.teamMembers[i].progressionOverview[j].timeCompleted) {
                                        var completedDate = new Date(data.teamMembers[i].progressionOverview[j].timeCompleted);
                                        if (data.startDate && (completedDate < data.startDate) || (data.endDate && (completedDate > data.endDate))) {
                                            data.teamMembers[i].progressionOverview.splice(j, 1);
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        data.teamMembers[i].progressionOverview.splice(j, 1);
                                    }
                                }
                            }
                        }
                    }
                },

                goToManagerView = function(manager) {
                    $uibModal.open({
                        templateUrl: 'scripts/apps/main-app/pages/team/team-individual/modal/team-individual-manager-modal.html',
                        controller: 'TeamIndividualManagerModalController as ctrl',
                        size: 'lg',
                        backdrop: 'static',
                        resolve: {
                            manager: function() {
                                return manager
                            }
                        }
                    });
                },

                goToMemberView = function(member, program) {
                    $uibModal.open({
                        templateUrl: 'scripts/apps/main-app/pages/team/modal/team-progression-details-modal.html',
                        controller: 'TeamProgressionDetailsModalController as ctrl',
                        size: 'lg',
                        backdrop: 'static',
                        resolve: {
                            member: function() {
                                return member
                            },

                            program: function() {
                                return program
                            }
                        }
                    });
                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },

                zend;

            init();


            return {
                data: data,
                managerHasManagers: managerHasManagers,
                teamMemberFilter: teamMemberFilter,
                isManagerFilter: isManagerFilter,
                isNotManagerFilter: isNotManagerFilter,
                search: search,
                setStartDate: setStartDate,
                setEndDate: setEndDate,
                goToManagerView: goToManagerView,
                goToMemberView: goToMemberView,
                notYetImplemented: notYetImplemented
            };
        }
    );

})();
