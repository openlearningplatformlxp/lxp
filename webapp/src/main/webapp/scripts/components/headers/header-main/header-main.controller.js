'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('HeaderMainController',
        function($rootScope, $scope, $state, $location, $timeout, $stateParams, Auth, Principal, resolvedAccountInfo, CatalogTypeService, NotificationService, NotificationUnreadService, FeedbackService, MessagingService, HeaderProfileService) {
            $scope.account = resolvedAccountInfo.account;
            $scope.searchValue = '';

            $scope.catalogs = [];

            $scope.feedback = {
                type: 'BUG',
                url: '',
                message: '',
                sent: false
            };

            var data = {
                    name: undefined,
                    navbar: {
                        controller: 'NavbarController as ctrl',
                        templateUrl: 'scripts/components/navbar/navbar.html',
                        helpUrl: appGlobal.config.get('app.config.supporturl')
                    },
                    notifications: []
                },
                getName = function(account) {
                    if (!account) {
                        return '';
                    }

                    var name = '';

                    if (account.firstName && account.firstName === account.lastName) {
                        name = account.firstName;
                    } else if (account.firstName && account.lastName) {
                        name = account.firstName + ' ' + account.lastName;
                    }

                    return name;
                },

                initPulling = function() {
                    setInterval(function() {
                        if ($scope.authenticated) {
                            NotificationUnreadService.getUnreadNotifications(function(response) {
                                $scope.notificationUnread = response.count;

                            }, function(error) {
                                console.log(error);
                            });
                        }
                    }, 60000);
                },

                init = function() {
                    $scope.authenticated = Principal.isAuthenticated();
                    if ($scope.authenticated) {
                        showStuff();
                    }
                    $scope.$on('onLoggedIn', function(event, data) {
                        $scope.authenticated = data.isAuthenticated;
                        $scope.account = data.account;
                        loadProfile();
                        showStuff();
                    });
                    $scope.$on('onLoggedOut', function(event, data) {
                        $scope.authenticated = Principal.isAuthenticated();
                        $scope.account = null;
                        $scope.profile = null;
                        $scope.profileImageUrl = null;
                    });

                    $scope.$on('onUpdatedImage', function(event, data) {
                        $scope.profileImageUrl = data;
                    });

                    loadProfile();
                },

                loadProfile = function() {
                    HeaderProfileService.getProfile(function(response) {
                        $scope.profile = response;
                        $scope.profileImageUrl = $scope.profile.pictureUrl + "?" + new Date().getTime();
                    }, function(error) {
                        console.log(error);
                    });
                },

                isAuthenticated = function() {
                    return Principal.isAuthenticated();
                },

                onCloseNotificationClick = function(notification, index) {
                    MessagingService.dismissMessage(notification).then(function success(response) {
                        data.notifications.splice(index, 1);
                    }, function(error) {
                        console.log(error);
                    });
                },

                loadNotifications = function() {
                    setTimeout(function() {
                        $scope.loadingNotifications = true;


                        NotificationService.getNotifications({},
                            function(response) {
                                $scope.loadingNotifications = false;
                                var notis = response;

                                data.notifications = [];

                                notis.forEach(function(noti) {
                                    var title = noti.title;
                                    var message = noti.message;
                                    if (noti.title === null || noti.title === '') {
                                        title = noti.message;
                                        message = '';
                                    }

                                    data.notifications.push({
                                        id: noti.id,
                                        title: title,
                                        message: message,
                                        type: noti.type,
                                        subjectId: noti.subjectId,
                                        link: noti.link,
                                        humanDate: moment(noti.dateTime).fromNow(),
                                        date: moment(noti.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                        pictureUrl: noti.pictureUrl
                                    });
                                })
                            },
                            function(error) {
                                console.log(error);
                            });
                    }, 500);
                },

                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },

                refreshFeedback = function() {
                    $scope.feedback = {
                        type: 'ENHANCEMENT',
                        url: '',
                        message: '',
                        sent: false
                    };

                    $scope.feedback.url = $location.path();

                    if ($scope.feedback.url === '/') {
                        $scope.feedback.url = '/overview';
                    }

                    $scope.feedback.sent = false;
                },

                goToHelpPage = function() {
                    window.open(data.navbar.helpUrl, '_blank');
                    $("#feedbackModal").modal('toggle');
                },

                search = function(value) {
                    //  If already on search view, retain original parameters
                    var params = {};

                    if ($location.path() === '/search/') {
                        params = $location.search();
                    }

                    params.q = encodeURIComponent(value);

                    $location.path('/search/').search(params);
                },

                selectCatalog = function(catalog) {
                    $location.path('catalog/t/' + catalog.id);
                },
                sendFeedback = function() {
                    if ($scope.feedback.message == null || $scope.feedback.message === '') {
                        $scope.feedback.messageRequired = true;
                    } else {
                        $scope.feedback.sent = true;
                        FeedbackService.sendFeedback($scope.feedback,
                            function(response) {
                                setTimeout(function() {
                                    $("#feedbackModal").modal('toggle');
                                }, 2000);
                            },
                            function(error) {
                                console.log(error);
                                $scope.feedback.sent = false;
                            });
                    }

                },

                getNumArray = function(num) {
                    return new Array(num);
                },

                showStuff = function() {
                    $scope.feedback.url = $location.path();

                    $scope.$on('onUpdatedSearch', function(event, data) {
                        $scope.searchValue = decodeURIComponent(data);
                    });

                    Principal.identity().then(function(identity) {
                        data.name = getName(identity);
                    });

                    $rootScope.$on('principal.identity.loaded', function(event, identity) {
                        data.name = getName(identity);
                    });

                    CatalogTypeService.getTypes({},
                        function(response) {
                            $scope.catalogs = response;
                        },
                        function(error) {
                            console.log(error);
                        });
                    initPulling();
                },

                dismissAll = function() {
                    MessagingService.dismissAllMessages(
                        function(response) {
                            data.notifications = [];
                            loadNotifications();
                        },
                        function(error) {
                            console.log(error);
                        });
                },

                logout = function() {
                    Auth.logout();

                    // TODO: SAC: the $timeout is a hack to get logout to work properly - fix this.

                    $timeout(function() {
                        $state.go('home', {}, {
                            reload: true
                        });
                    }, 100);
                };

            init();

            return {
                data: data,
                getNumArray: getNumArray,
                search: search,
                selectCatalog: selectCatalog,
                sendFeedback: sendFeedback,
                refreshFeedback: refreshFeedback,
                goToHelpPage: goToHelpPage,
                onCloseNotificationClick: onCloseNotificationClick,
                loadNotifications: loadNotifications,
                notYetImplemented: notYetImplemented,
                isAuthenticated: isAuthenticated,
                dismissAll: dismissAll,
                logout: logout
            };
        }
    );

    module.factory('CatalogTypeService', function($resource) {
        return $resource('api/catalog/types', {}, {
            getTypes: {
                method: 'GET',
                isArray: true
            }
        });
    });
    module.factory('NotificationService', function($resource) {
        return $resource('api/notification/', {}, {
            getNotifications: {
                method: 'GET',
                isArray: true
            }
        });
    });
    module.factory('NotificationUnreadService', function($resource) {
        return $resource('api/notification/unread/pull', {}, {
            getUnreadNotifications: {
                method: 'GET'
            }
        });
    });

    module.factory('FeedbackService', function($resource) {
        return $resource('api/feedback/', {}, {
            sendFeedback: {
                method: 'POST'
            }
        });
    });


    module.factory('HeaderProfileService', function($resource) {
        return $resource('api/profile/', {}, {
            getProfile: {
                method: 'GET'
            }
        });
    });

})();
