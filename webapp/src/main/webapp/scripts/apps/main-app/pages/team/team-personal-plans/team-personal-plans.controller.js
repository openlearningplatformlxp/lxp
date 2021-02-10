'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('TeamPersonalPlansController',
        function($http, $location, $anchorScroll, $uibModal, TeamService) {

            var data = {
                    isLoading: false,
                    startDate: null,
                    endDate: null,
                    stats: null
                },

                init = function() {
                    data.isLoading = true;
                    data.startDate = null;
                    data.endDate = null;
                    TeamService.getTeamPLPData({}).then(function(response) {
                        prepareTeamProgressionData(response);
                        data.originalProgressionOverview = angular.copy(data.progressionOverview);
                        data.isLoading = false;
                        data.stats = response;
                        data.stats.individuals = data.stats.individuals.map(function(ind) {
                            var name = ind.name.split(' ');
                            return jQuery.extend({
                                firstName: name[0],
                                lastName: name[1]
                            }, ind);
                        });
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
                        memberStats;

                    data.progressionOverview = teamData.progressOverview;

                    for (i = 0, iLen = data.progressionOverview.length; i < iLen; i++) {
                        progressionStats = data.progressionOverview[i];

                        for (j = 0, jLen = progressionStats.teamMemberCompletionList.length; j < jLen; j++) {
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

                teamProgressionFilter = function(teamProgression) {
                    var type = teamProgression.program ? teamProgression.program.type || "LEARNING_PATH" : null;

                    // Check search
                    if (data.searchValue && data.searchValue.length && teamProgression.program.title.toUpperCase().indexOf(data.searchValue) === -1) {
                        return false;
                    }

                    return true;
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
                teamMemberFilter: teamMemberFilter,
                isManagerFilter: isManagerFilter,
                isNotManagerFilter: isNotManagerFilter,
                search: search,
                setStartDate: setStartDate,
                setEndDate: setEndDate,
                goToMemberView: goToMemberView,
                notYetImplemented: notYetImplemented
            };
        }
    );

})();
