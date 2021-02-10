'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('MainController',
        function($scope, $state, $stateParams, $location, $http, MainService, CardService, MessagingService, CourseEnrollmentService, Principal, resolvedAccountInfo, CourseService, AlertsService) {

            $scope.cardView = CardService.getView();


            var data = {
                returnPath: $location.url()
            };

            var createDismissButton = function(notificationId) {
                return [{
                    text: "Dismiss",
                    action: "api/message/dismiss/" + notificationId,
                    order: 0
                }];
            };

            var initPulling = function() {
                setInterval(function() {
                    MessagingService.getMessages({
                        type: 'NOTIFICATION'
                    }).then(function success(response) {
                        $scope.notification = response;

                        if ($scope.notification) {
                            $scope.notification.buttons = eval($scope.notification.actionButtons) || createDismissButton($scope.notification.id);
                        }

                    }, function(error) {
                        console.log(error);
                    });
                }, 60000); // Updated to 60 sec
            };

            var onButtonClick = function(button) {

                $http.post(button.action).then(function(response) {
                    $scope.notification = null;
                }).catch(function(error) {
                    console.log(error);
                });
            };

            var init = function() {
                    var i,
                        len,
                        appointment;

                    $scope.loading = true;
                    Principal.identity().then(function(account) {
                        $scope.account = account;
                    });
                    $scope.$on('onLoggedIn', function(event, data) {
                        $scope.account = data.account;
                    });
                    $scope.$on('onUpdatedCardView', function(event, data) {
                        $scope.cardView = data;
                    });
                    MainService.getHomeData(function(response) {
                        $scope.loading = false;
                        $scope.home = response;

                        $scope.home.currentItemTimeLeft = CourseService.convertMinutesDurationToHoursAndMinutes($scope.home.currentItemMinutesLeft);

                        if ($scope.home.nextAppointments) {
                            for (i = 0, len = $scope.home.nextAppointments.length; i < len; i += 1) {
                                // Augment duration for display
                                appointment = $scope.home.nextAppointments[i];
                                appointment.duration = CourseService.convertMinutesDurationToHoursAndMinutes(appointment.durationMinutes);
                            }
                        }

                        $scope.learningPathCards = new Array();
                        $scope.personalLearningPathCards = new Array();
                        $scope.courseCards = new Array();
                        $scope.classroomCards = new Array();


                        $scope.home.learningPaths.forEach(function(path) {
                            $scope.learningPathCards.push(CardService.buildCard(path));
                        });
                        $scope.home.personalLearningPaths.forEach(function(path) {
                            $scope.personalLearningPathCards.push(CardService.buildCard(path));
                        });
                        $scope.home.courses.forEach(function(path) {
                            $scope.courseCards.push(CardService.buildCard(path));
                        });

                        if ($scope.home.classrooms != null) {
                            $scope.home.classrooms.forEach(function(path) {
                                $scope.classroomCards.push(CardService.buildCard(path));
                            });
                        }

                        if (Principal.isAuthenticated()) {
                            Principal.hasAuthority('ROLE_CMS_EDITOR').then(function(hasAuthority) {
                                data.showOpenInLms = hasAuthority;
                            })
                        }

                    }, function(error) {
                        console.log(error);
                    });

                    initPulling();
                },

                resumePath = function() {
                    if ($scope.home.nextAppointments && $scope.home.nextAppointments.length) {
                        resumeAppointment($scope.home.nextAppointments[0]);
                    }
                },

                resumeAppointment = function(appointment) {
                    CourseEnrollmentService.navigateToCourseWithEnrollment($scope.home.currentItem.id, appointment.courseId, appointment.enrolled, appointment.inProgress, appointment.completed, 'home', false);
                },

                searchByType = function(type) {
                    $location.path('search/').search({
                        type: type,
                        status: 'IN_PROGRESS'
                    });
                };

            init();

            var notYetImplemented = function() {
                alert("Not yet Implemented");
            };

            var dropCurrentItem = function() {
                CourseEnrollmentService.dropProgram({
                    programId: $scope.home.currentItem.id
                }).then(function(data) {
                    AlertsService.alert({
                        title: '',
                        text: 'You have dropped from the path',
                    });
                    $state.transitionTo($state.current, $stateParams, {
                        reload: true,
                        inherit: false,
                        notify: true
                    });
                    return data
                });
            };

            var goToCurrentItem = function() {
                var currentItem = $scope.home.currentItem,
                    itemType = (currentItem && currentItem.type) ? currentItem.type : null,
                    itemId = currentItem ? currentItem.id : null;
                if (itemType && itemId) {
                    if (itemType === "LEARNING_PATH") {
                        $state.go("learning-path", {
                            id: itemId,
                            source: 'home'
                        });
                    }
                    if (itemType === "COURSE") {
                        $state.go("course-totara", {
                            courseId: itemId,
                            source: "home"
                        });
                        /*
                        $state.go("course-overview", {
                            courseId: itemId,
                            returnPath: data.returnPath
                        });
                        */
                    }
                }
            };

            return {
                data: data,
                notYetImplemented: notYetImplemented,
                onButtonClick: onButtonClick,
                resumePath: resumePath,
                resumeAppointment: resumeAppointment,
                searchByType: searchByType,
                goToCurrentItem: goToCurrentItem,
                dropCurrentItem: dropCurrentItem
            };
        }
    );

    module.factory('MainService', function($resource) {
        return $resource('api/main', {}, {
            getHomeData: {
                method: 'GET'
            }
        });
    });
})();
