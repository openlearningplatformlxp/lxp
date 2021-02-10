'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('TeamCourseController',
        function($http, $location, $anchorScroll, $uibModal, resolvedProgressionType, TeamService) {

            var data = {
                    title: resolvedProgressionType === "COURSE" ? "Course" : "Learning Path",
                    subtitle: resolvedProgressionType === "COURSE" ? " A list of all the in-progress and completed courses for your direct-reports." : "A list of all the learning paths and progress for your direct-reports.",
                    searchPlaceholderText: resolvedProgressionType === "COURSE" ? "What course are you looking for?" : "What learning path are you looking for?",
                    progressionOverview: [],
                    progressionType: resolvedProgressionType,
                    isLoading: false,
                    startDate: null,
                    endDate: null,
                    originalProgressionOverview: null
                },

                init = function() {
                    data.isLoading = true;
                    data.startDate = null;
                    data.endDate = null;
                    TeamService.getTeamData(data.progressionType, {}).then(function(response) {
                        prepareTeamProgressionData(response);
                        data.originalProgressionOverview = angular.copy(data.progressionOverview);
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

                    // TODO: (WJK) Replace with server-side data conversion? (send in correct format initially)

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

                isManagerFilter = function(teamMember) {
                    return teamMember.manager;
                },
                isNotManagerFilter = function(teamMember) {
                    return !teamMember.manager;
                },

                teamProgressionFilter = function(teamProgression) {
                    var type = teamProgression.program ? teamProgression.program.type || "LEARNING_PATH" : null;

                    // Check type
                    if (type !== data.progressionType) {
                        return false;
                    }

                    // Check search
                    if (data.searchValue && data.searchValue.length && teamProgression.program.title.toUpperCase().indexOf(data.searchValue) === -1) {
                        return false;
                    }

                    return true;
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
                    data.progressionOverview = angular.copy(data.originalProgressionOverview);

                    if (!data.startDate && !data.endDate) {
                        return;
                    }

                    if (data.progressionOverview && data.progressionOverview.length > 0) {
                        var i = data.progressionOverview.length;
                        while (i--) {
                            if (data.progressionOverview[i].percentComplete == 0) {
                                data.progressionOverview.splice(i, 1);
                            } else {
                                data.progressionOverview[i].percentComplete = 100;
                                if (data.progressionOverview[i].teamMemberCompletionList && data.progressionOverview[i].teamMemberCompletionList.length > 0) {
                                    var j = data.progressionOverview[i].teamMemberCompletionList.length;
                                    while (j--) {
                                        if (data.progressionOverview[i].teamMemberCompletionList[j].completedDate) {
                                            var completedDate = new Date(data.progressionOverview[i].teamMemberCompletionList[j].completedDate);
                                            if (data.startDate && (completedDate < data.startDate) || (data.endDate && (completedDate > data.endDate))) {
                                                data.progressionOverview[i].teamMemberCompletionList.splice(j, 1);
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            data.progressionOverview[i].teamMemberCompletionList.splice(j, 1);
                                        }
                                    }
                                    if (data.progressionOverview[i].teamMemberCompletionList.length == 0) {
                                        data.progressionOverview.splice(i, 1);
                                    }
                                }
                            }
                        }
                    }
                },

                goToManagerView = function(manager, type) {
                    $uibModal.open({
                        templateUrl: 'scripts/apps/main-app/pages/team/team-course/modal/team-course-manager-modal.html',
                        controller: 'TeamCourseManagerModalController as ctrl',
                        size: 'lg',
                        backdrop: 'static',
                        resolve: {
                            manager: function() {
                                return manager
                            },
                            type: function() {
                                return type
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
                teamProgressionFilter: teamProgressionFilter,
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
