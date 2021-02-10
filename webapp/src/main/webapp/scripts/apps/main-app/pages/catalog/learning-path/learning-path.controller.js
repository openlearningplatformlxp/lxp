'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('LearningPathController',
        function($state, $location, $stateParams, resolvedAccountInfo, resolvedProgramOverview, OverviewCardService, CourseEnrollmentService, CourseService, PersonalPlanService, AlertsService) {

            var data = {
                    // My Personal programs should be editable
                    account: resolvedAccountInfo.account,
                    program: resolvedProgramOverview.program,
                    manager: resolvedProgramOverview.program.manager,
                    shared: resolvedProgramOverview.program.shared,
                    shares: resolvedProgramOverview.program.shares,
                    sharedWithManager: resolvedProgramOverview.program.sharedWithManager,
                    sharedWithReports: resolvedProgramOverview.program.sharedWithReports,
                    accountId: resolvedProgramOverview.program.accountId,
                    ownerId: resolvedProgramOverview.program.ownerId,
                    personal: resolvedProgramOverview.program.personal,
                    edit: resolvedProgramOverview.program.personal && !resolvedProgramOverview.create && !resolvedProgramOverview.program.archived,
                    create: resolvedProgramOverview.create,
                    learningPath: OverviewCardService.buildCard(resolvedProgramOverview.program, resolvedProgramOverview.percentComplete),
                    courseSets: resolvedProgramOverview.courseSets,
                    nextAppointments: resolvedProgramOverview.nextAppointments,
                    members: resolvedProgramOverview.teamMembers,
                    htmlBlock: resolvedProgramOverview.htmlBlock,
                    tagsTypes: resolvedProgramOverview.tagsTypes,
                    showResume: !!resolvedProgramOverview.program.enrolled && resolvedProgramOverview.nextAppointments && resolvedProgramOverview.nextAppointments.length,
                    showEnroll: resolvedProgramOverview.program.enrolSelfEnabled && !resolvedProgramOverview.program.enrolled,
                    totalNumOfCourses: resolvedProgramOverview.totalNumOfCourses,
                    returnPath: $state.params.returnPath,
                    popupLearningPath: {},
                    shareLearningPath: {},
                    shareLearningPathLists: [{
                            name: "Your direct reports",
                            items: []
                        },
                        {
                            name: "This plan is shared with",
                            items: []
                        },
                    ],
                    selectedCatalog: null,
                    taskActivity: null,
                    textEntry: {
                        sent: false,
                        textEntryId: null
                    }
                },


                init = function() {
                    var i,
                        len,
                        appointment;

                    data.totalDuration = CourseService.convertMinutesDurationToHoursAndMinutes(resolvedProgramOverview.totalMinutes);

                    if (data.nextAppointments) {
                        for (i = 0, len = data.nextAppointments.length; i < len; i += 1) {
                            // Augment duration for display
                            appointment = data.nextAppointments[i];
                            appointment.duration = CourseService.convertMinutesDurationToHoursAndMinutes(appointment.durationMinutes);
                        }
                    }

                    if (data.create) {
                        data.ownerId = data.accountId;
                    }

                    refreshDataMembers();
                },

                refreshDataMembers = function() {
                    if (data.members) {
                        data.shareLearningPathLists[0].items = [];
                        data.shareLearningPathLists[1].items = [];
                        data.members.forEach(function(member) {
                            var targetList = data.shareLearningPathLists[0];
                            if (data.shared && data.sharedWithReports) {
                                data.shares.forEach(function(share) {
                                    if (share.pk.sharedUserId === member.userId) {
                                        targetList = data.shareLearningPathLists[1];
                                    }
                                    // It's the same for all entries
                                    data.shareLearningPath.messageReports = share.message;
                                    data.shareLearningPath.dueDate = moment(share.dueDate, "YYYY-MM-DDThh:mm:ssZ").format('MMDDYYYY');
                                });

                            }
                            targetList.items.push({
                                id: member.userId,
                                label: member.displayName
                            });
                        });
                    }
                },

                isCourseLocked = function(course) {
                    return course.status && course.status.locked;
                },

                isCourseInProgress = function(course) {
                    return course.status && course.status.inProgress;
                },

                isCourseCompleted = function(course) {
                    return course.status && course.status.completed;
                },

                isCourseEnrolled = function(course) {
                    return course.status && course.status.enrolled;
                },

                getTotalCourses = function(courseSets, status) {
                    var count = 0;
                    courseSets.forEach(function(set) {
                        if (set.courses != null) {
                            set.courses.forEach(function(course) {
                                if (status === 'NOT_STARTED' && !isCourseInProgress(course) && !isCourseCompleted(course)) {
                                    count++;
                                } else if (status === 'IN_PROGRESS' && isCourseInProgress(course)) {
                                    count++;
                                } else if (status === 'COMPLETED' && isCourseCompleted(course)) {
                                    count++;
                                }
                            });
                        }
                    });
                    return count;
                },

                getTotalCoursesNotStarted = function(courseSets) {
                    return getTotalCourses(courseSets, 'NOT_STARTED');
                },

                getTotalCoursesInProgress = function(courseSets) {
                    return getTotalCourses(courseSets, 'IN_PROGRESS');
                },

                getTotalCoursesComplete = function(courseSets) {
                    return getTotalCourses(courseSets, 'COMPLETED');
                },

                isManualCompletable = function(course) {
                    if (!data.program.archived && data.edit && data.accountId == data.ownerId) {
                        if (course.cms === 'LMS') {
                            return !(course.type === 'COURSE' || course.type === 'PROGRAM_LINK');
                        } else {
                            // External courses are manually completable
                            return true;
                        }
                    }
                    return false;
                },

                isIsLinkableActivity = function(course) {
                    if (course.type === 'OFFLINE_TASK') {
                        return false;
                    }
                    if (!data.create) {
                        if (course.type === 'WEB_TASK' || course.type === 'WIKIPAGE') {
                            return true;
                        }
                    }
                    return true;
                },

                refreshProgress = function() {
                    var completedCourses = 0;
                    data.courseSets.forEach(function(cs, courseSetIndex) {
                        cs.courses.forEach(function(c, courseIndex) {
                            completedCourses = isCourseCompleted(c) ? completedCourses + 1 : completedCourses;
                        });
                    });
                    if (data.totalNumOfCourses === 0) {
                        data.learningPath.percentComplete = 0;
                    } else {
                        data.learningPath.percentComplete = Math.round((completedCourses / data.totalNumOfCourses) * 100);
                    }
                },
                toggleManualCompletion = function(courseSet, courseSetIndex, index, course) {
                    var completed = isCourseCompleted(course);
                    var newStatus = completed != null && completed ? 0 : 100;
                    CourseEnrollmentService.toggleManualCompletion({
                        itemId: course.itemId,
                        status: newStatus
                    }).then(function success(response) {
                        if (newStatus === 100) {
                            course.status = {
                                completed: true
                            };
                        } else {
                            course.status = {
                                completed: false,
                                inProgress: true
                            };
                        }
                        data.courseSets[courseSetIndex].courses[index] = course;
                        refreshProgress();

                    }, function(error) {
                        console.log(error);
                    });
                },
                goBack = function() {
                    if (data.returnPath) {
                        $location.url(data.returnPath);
                        return;
                    }
                },

                cancelCreate = function() {
                    if (data.learningPath.id != null) {
                        data.create = false;
                        data.edit = true;
                        $state.transitionTo($state.current, {
                            id: data.learningPath.id,
                            personal: true
                        }, {
                            reload: true,
                            inherit: false,
                            notify: true
                        });
                    } else {
                        $state.go("profile-learning", {});
                    }
                },

                goToTopic = function(topic) {
                    $location.path('search/').search({
                        topic: topic
                    });
                },

                resumePath = function() {
                    if (data.nextAppointments && data.nextAppointments.length) {
                        resumeAppointment(data.nextAppointments[0]);
                    }
                },

                enrollInProgram = function() {
                    CourseEnrollmentService.enrollInProgram({
                        programId: resolvedProgramOverview.program.id
                    }).then(function success(response) {
                        AlertsService.alert({
                            title: '',
                            text: 'You have enrolled into the learning path',
                        });
                        resolvedProgramOverview.program.enrolled = true;
                        data.showResume = resolvedProgramOverview.nextAppointments && resolvedProgramOverview.nextAppointments.length;
                        data.showEnroll = false;
                    }, function(error) {
                        console.log(error);
                    });
                },

                resumeAppointment = function(appointment) {
                    if (!resolvedProgramOverview.program.enrolled) {
                        return;
                    }
                    CourseEnrollmentService.navigateToCourseWithEnrollment(resolvedProgramOverview.program.id, appointment.courseId, appointment.enrolled, appointment.inProgress, appointment.completed, 'learning-path', data.personal);
                },

                resumeCourse = function(course) {
                    if (!data.create) {
                        if (course.type === 'PROGRAM_LINK') {
                            $state.go("learning-path", {
                                id: course.id,
                                returnPath: '/catalog/learning-path/' + resolvedProgramOverview.program.id
                            });
                            return;
                        } else if (course.type === 'TEXT_ENTRY' || course.type === 'OFFLINE_TASK' || course.type === 'WEB_TASK') {
                            return;
                        } else if (course.type === 'WEB_TASK') {
                            window.open(course.activityValue, '_blank');
                            return;
                        } else if (course.type === 'WIKIPAGE') {
                            window.open('/#/pages' + course.activityValue, '_blank');
                            return;
                        } else if (!resolvedProgramOverview.program.enrolled || isCourseLocked(course)) {
                            return;
                        } else if (resolvedProgramOverview.program.personal) {
                            if (course.cms === 'LMS') {
                                CourseEnrollmentService.navigateToCourse(course.id, isCourseInProgress(course), isCourseCompleted(course), data.program.id, 'learning-path', data.personal);
                            }
                            // TODO add support for complete external sources
                            return;
                        }
                        CourseEnrollmentService.navigateToCourseWithEnrollment(resolvedProgramOverview.program.id, course.id, isCourseEnrolled(course), isCourseInProgress(course), isCourseCompleted(course), 'learning-path', data.personal);
                    }
                },
                saveTextEntry = function(course) {
                    data.textEntry.sent = true;
                    if (data.textEntry.message != null) {
                        CourseEnrollmentService.saveTextEntry({
                            programId: resolvedProgramOverview.program.id,
                            activityId: course.activityId,
                            textEntryId: course.id,
                            message: data.textEntry.message
                        }).then(function(d) {
                            data.textEntry.success = true;
                            course.status.completed = true;
                        });
                    }
                },

                dropPath = function(course) {
                    if (!resolvedProgramOverview.program.enrolled) {
                        return;
                    }
                    CourseEnrollmentService.dropProgram({
                        programId: resolvedProgramOverview.program.id
                    }).then(function(data) {
                        AlertsService.alert({
                            title: '',
                            text: 'You have dropped from the path',
                        });
                        $state.transitionTo($state.currenth, $stateParams, {
                            reload: true,
                            inherit: false,
                            notify: true
                        });
                        resolvedProgramOverview.program.enrolled = false;
                        data.showResume = false;
                        data.showEnroll = true;
                        return data;
                    });
                },

                isDueDate = function(item) {
                    return item.dueDate != null && moment(item.dueDate, 'MM/DD/YYYY').isBefore(moment());
                },

                // Get the icon name to show on the activity row
                getCourseCompletionIconClass = function(course) {
                    var iconClass = "circle"; // default open but not started

                    if (!resolvedProgramOverview.program.enrolled || isCourseLocked(course)) {
                        iconClass = "lock";
                    } else if (isCourseCompleted(course)) {
                        iconClass = "check";
                    } else if (isCourseInProgress(course)) {
                        iconClass = "disc";
                    }

                    return iconClass;
                },

                // get the class name for color display on the activity row
                getCourseCompletionIconDisplayClass = function(course) {
                    var cls = "course-list-status-notstarted";

                    if (isCourseCompleted(course)) {
                        cls = "course-list-status-complete";
                    } else if (isCourseInProgress(course)) {
                        cls = "course-list-status-inprogress";
                    }

                    return cls;
                },

                changeAndClearActivityStep = function(step, index) {
                    data.selectedCatalog = null;
                    data.taskActivity = null;
                    data.webActivity = null;
                    changeActivityStep(step, index);
                },

                changeActivityStep = function(step, index) {
                    data.activityStep = step;
                    data.courseSetIndex = index;
                },

                clonePersonalPlan = function() {
                    PersonalPlanService.clonePersonalPlan(data.learningPath.id).then(function(response) {
                        AlertsService.alert({
                            title: '',
                            text: 'Cloned personal path successfully!',
                        });
                        $state.transitionTo($state.current, {
                            id: response.data.id,
                            personal: true
                        }, {
                            reload: true,
                            inherit: false,
                            notify: true
                        });
                    }).catch(function(response) {
                        if (response.status == 419) {
                            AlertsService.alert({
                                title: '',
                                text: 'You can\'t cloned this personal path because you can\'t have more than ' + response.data + ' paths!',
                            });
                        }
                    });
                },

                savePersonalPlan = function() {
                    data.learningPath.courseSets = data.courseSets;
                    data.learningPath.courseSets.forEach(function(e, index) {
                        e.sortOrder = index;
                        if (e.courses != null) {
                            e.courses.forEach(function(c, index) {
                                c.sortOrder = index;
                            });
                        }
                    });
                    PersonalPlanService.saveOrUpdatePersonalPlan(data.learningPath).then(function(response) {
                        data.courseSets = response.data.courseSets;
                        if (data.learningPath.id == null) {
                            AlertsService.alert({
                                title: '',
                                text: 'You have saved your personal path!',
                            });
                            $state.go("learning-path", {
                                id: response.data.id,
                                personal: true
                            });
                        } else {
                            AlertsService.alert({
                                title: '',
                                text: 'You have updated your personal path!',
                            });
                        }
                        data.learningPath.id = response.data.id;
                        data.create = false;
                        data.edit = true;
                    });
                },

                cloneLP = function() {
                    angular.copy(data.learningPath, data.popupLearningPath);
                    data.popupLearningPath.popupHeader = 'Personal Learning Path Properties';
                },

                buildSection = function() {
                    data.popupLearningPath.popupHeader = 'Section Properties';
                    data.popupLearningPath.section = true;
                    data.popupLearningPath.isNew = true;
                    data.popupLearningPath.title = '';
                    data.popupLearningPath.description = '';
                    data.popupLearningPath.dueDate = '';
                },

                editSection = function(section, index) {
                    data.popupLearningPath.popupHeader = 'Section Properties';
                    data.popupLearningPath.section = true;
                    data.popupLearningPath.isNew = false;
                    data.popupLearningPath.index = index;
                    data.popupLearningPath.title = section.name;
                    data.popupLearningPath.description = section.summary;
                    data.popupLearningPath.dueDate = section.dueDate;
                },

                updatedDueDate = function(date) {
                    data.learningPath.dueDateUpdated = date;
                },

                updateFromPopup = function() {
                    if (data.popupLearningPath.section) {
                        if (data.popupLearningPath.isNew) {
                            if (data.courseSets.length > 0) {
                                // TODO next set operator is not in wireframe
                                data.courseSets[data.courseSets.length - 1].nextSetOperator = 'NEXTSETOPERATOR_AND';
                            }
                            data.courseSets.push({
                                name: data.popupLearningPath.title,
                                summary: data.popupLearningPath.description
                            });
                        } else {
                            data.courseSets[data.popupLearningPath.index].name = data.popupLearningPath.title;
                            data.courseSets[data.popupLearningPath.index].summary = data.popupLearningPath.description;
                            if (data.popupLearningPath.dueDate) {
                                data.courseSets[data.popupLearningPath.index].dueDate = moment(data.popupLearningPath.dueDate, "MMDDYYYY").format('MM/DD/YYYY');
                            } else {
                                data.courseSets[data.popupLearningPath.index].dueDate = null;
                            }
                        }
                    } else {
                        data.learningPath.title = data.popupLearningPath.title;
                        data.learningPath.description = data.popupLearningPath.description;
                        if (data.popupLearningPath.dueDate) {
                            data.learningPath.dueDate = moment(data.popupLearningPath.dueDate, "MMDDYYYY").format('MM/DD/YYYY');
                        } else {
                            data.learningPath.dueDate = null;
                        }
                    }
                },

                saveActivity = function() {
                    var newActivity = null;

                    if (data.activityStep === 'OFFLINE_TASK' || data.activityStep === 'WEB_TASK') {
                        var item = data.activityStep === 'OFFLINE_TASK' ? data.taskActivity : data.webActivity;
                        if (item != null) {
                            var dueDate = null;
                            if (item.dueDate) {
                                dueDate = moment(item.dueDate, "MMDDYYYY").format('MM/DD/YYYY');
                            }
                            item.type = data.activityStep;
                            if (data.courseSets[data.courseSetIndex].courses == null) {
                                data.courseSets[data.courseSetIndex].courses = [];
                            }
                            status = {
                                enrolled: true,
                                inProgress: true
                            };
                            newActivity = {
                                type: item.type,
                                cms: 'LMS',
                                fullName: item.title,
                                description: item.description,
                                contentType: 'COURSE',
                                contentCaption: data.activityStep === 'OFFLINE_TASK' ? 'Offline Task' : 'Web Activity',
                                activityValue: item.url,
                                status: status,
                                dueDate: dueDate
                            };
                        }
                    } else if (data.activityStep === 'COURSE') {
                        if (data.selectedCatalog != null) {
                            var item = data.selectedCatalog.description;

                            var dueDate = null;
                            if (item.dueDate) {
                                dueDate = moment(item.dueDate, "MMDDYYYY").format('MM/DD/YYYY');
                            }

                            if (data.courseSets[data.courseSetIndex].courses == null) {
                                data.courseSets[data.courseSetIndex].courses = [];
                            }
                            if (item.type === 'LEARNING_PATH') {
                                item.type = 'PROGRAM_LINK';
                            }
                            var status = null;
                            if (item.cms !== 'LMS') {
                                status = {
                                    enrolled: true,
                                    inProgress: true
                                }
                            }
                            newActivity = {
                                id: item.externalId != null ? item.externalId.split("{}")[1] : item.id,
                                cms: item.cms,
                                type: item.type,
                                fullName: item.title,
                                title: item.title,
                                ceCredits: item.ceCredits,
                                duration: item.duration,
                                description: item.description,
                                contentType: item.type,
                                contentCaption: item.type,
                                activityValue: item.externalUrl,
                                status: status,
                                dueDate: dueDate
                            };
                        }
                    }
                    if (newActivity != null) {
                        if (data.editIndex != null) {
                            data.courseSets[data.courseSetIndex].courses[data.editIndex] = newActivity;
                            data.editIndex = null;
                        } else {
                            data.courseSets[data.courseSetIndex].courses.push(newActivity);
                            data.totalNumOfCourses++;
                            refreshProgress();
                        }
                    }
                },

                editActivity = function(activity, courseSetIndex, activityIndex) {
                    var dueDate = null;
                    if (activity.dueDate) {
                        dueDate = moment(activity.dueDate, "MMDDYYYY").format('MM/DD/YYYY');
                    }
                    var obj = {
                        title: activity.fullName,
                        description: activity.description,
                        url: activity.activityValue,
                        dueDate: dueDate
                    };
                    if (activity.type === 'OFFLINE_TASK') {
                        data.taskActivity = obj;
                    } else if (activity.type === 'WEB_TASK') {
                        data.webActivity = obj;
                    } else {
                        data.selectedCatalog = {
                            description: obj
                        };
                    }
                    data.editIndex = activityIndex;
                    changeActivityStep(activity.type, courseSetIndex);
                },

                moveActivity = function(activity, courseSetIndex, activityIndex, targetIndex) {
                    if (targetIndex < 0 || targetIndex >= data.courseSets[courseSetIndex].courses.length) {
                        return;
                    }
                    var self = data.courseSets[courseSetIndex].courses[activityIndex];
                    var target = data.courseSets[courseSetIndex].courses[targetIndex];

                    data.courseSets[courseSetIndex].courses[targetIndex] = self;
                    data.courseSets[courseSetIndex].courses[activityIndex] = target;
                },

                removeActivity = function(activity, courseSetIndex, activityIndex) {
                    if (data.courseSets[courseSetIndex].courses != null) {
                        data.courseSets[courseSetIndex].courses.splice(activityIndex, 1);
                        data.totalNumOfCourses--;
                        refreshProgress();
                    }
                },

                moveSection = function(course, courseSetIndex, targetIndex) {
                    if (targetIndex < 0 || targetIndex >= data.courseSets.length) {
                        return;
                    }
                    var self = data.courseSets[courseSetIndex];
                    var target = data.courseSets[targetIndex];

                    data.courseSets[targetIndex] = self;
                    data.courseSets[courseSetIndex] = target;
                },

                openSharePopup = function() {
                    data.shareLearningPath = {};
                },

                openArchivePopup = function() {
                    AlertsService.confirm({
                        title: 'Are you sure that you want to archive this personal learning path?',
                        text: 'Any courses or paths which you enrolled on the LMS will be unaffected.',
                        buttonOk: {
                            onClick: function() {
                                PersonalPlanService.archivePlan({
                                    id: data.learningPath.id
                                }).then(function(response) {
                                    $state.transitionTo($state.current, {
                                        id: data.learningPath.id,
                                        personal: true
                                    }, {
                                        reload: true,
                                        inherit: false,
                                        notify: true
                                    });
                                    return true;
                                });
                            }
                        }
                    });
                },

                sharePersonalPlan = function(type) {
                    if (type === 'MANAGER') {
                        PersonalPlanService.shareWithManager({
                            itemId: data.learningPath.id,
                            message: data.shareLearningPath.message
                        }).then(function(response) {
                            AlertsService.alert({
                                title: '',
                                text: 'You have shared your personal path!',
                                button: {
                                    onClick: function() {
                                        $state.transitionTo($state.current, {
                                            id: data.learningPath.id,
                                            personal: true
                                        }, {
                                            reload: true,
                                            inherit: false,
                                            notify: true
                                        });
                                        return true;
                                    }
                                }
                            });
                        });
                    } else {
                        if (data.shareLearningPath.messageReports == null || data.shareLearningPath.messageReports === "") {
                            return false;
                        }
                        // DIRECT REPORTS
                        var ids = [];
                        data.shareLearningPathLists[1].items.forEach(function(tm) {
                            ids.push(tm.id)
                        });
                        //
                        PersonalPlanService.shareWithDirectReports({
                            itemId: data.learningPath.id,
                            directReportIds: ids,
                            message: data.shareLearningPath.messageReports,
                            dueDate: moment(data.shareLearningPath.dueDate, "MMDDYYYY").format('YYYY-MM-DDThh:mm:ss')
                        }).then(function(response) {
                            AlertsService.alert({
                                title: '',
                                text: 'You have shared your personal path!',
                                button: {
                                    onClick: function() {
                                        $state.transitionTo($state.current, {
                                            id: data.learningPath.id,
                                            personal: true
                                        }, {
                                            reload: true,
                                            inherit: false,
                                            notify: true
                                        });
                                        return true;
                                    }
                                }
                            });
                        });
                    }
                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                };

            init();

            return {
                data: data,
                isCourseLocked: isCourseLocked,
                cloneLP: cloneLP,
                editSection: editSection,
                updatedDueDate: updatedDueDate,
                updateFromPopup: updateFromPopup,
                buildSection: buildSection,
                clonePersonalPlan: clonePersonalPlan,
                savePersonalPlan: savePersonalPlan,
                changeAndClearActivityStep: changeAndClearActivityStep,
                changeActivityStep: changeActivityStep,
                saveActivity: saveActivity,
                removeActivity: removeActivity,
                editActivity: editActivity,
                moveActivity: moveActivity,
                moveSection: moveSection,
                cancelCreate: cancelCreate,
                goBack: goBack,
                dropPath: dropPath,
                resumePath: resumePath,
                enrollInProgram: enrollInProgram,
                resumeAppointment: resumeAppointment,
                resumeCourse: resumeCourse,
                saveTextEntry: saveTextEntry,
                isDueDate: isDueDate,
                isIsLinkableActivity: isIsLinkableActivity,
                getCourseCompletionIconClass: getCourseCompletionIconClass,
                getCourseCompletionIconDisplayClass: getCourseCompletionIconDisplayClass,
                goToTopic: goToTopic,
                getTotalCoursesNotStarted: getTotalCoursesNotStarted,
                getTotalCoursesInProgress: getTotalCoursesInProgress,
                getTotalCoursesComplete: getTotalCoursesComplete,
                isCourseCompleted: isCourseCompleted,
                isManualCompletable: isManualCompletable,
                toggleManualCompletion: toggleManualCompletion,
                openSharePopup: openSharePopup,
                openArchivePopup: openArchivePopup,
                sharePersonalPlan: sharePersonalPlan,
                notYetImplemented: notYetImplemented
            };
        }
    );
})();
