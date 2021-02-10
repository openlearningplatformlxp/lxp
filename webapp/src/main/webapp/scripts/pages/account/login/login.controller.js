'use strict';

(function() {
    var module = angular.module('app.pages');

    module.controller('LoginController',
        function(resolvedError, $rootScope, $state, $timeout, Auth, Principal, BUILD, $stateParams) {

            //only do this is "samlRedirect.enabled" is true
            if (appGlobal.config.get('saml.redirect.enabled')) {
                //before doing anything check if the value "?manual" is present on the url,
                //if "?manual" is present, stay on the page
                //if not, redirect the user to "/saml/login"
                //  if($stateParams.manual){
                //stay here do nothing
                //   }else{
                //      window.location.href = appGlobal.config.get('saml.redirect.url');
                //  }
            }

            var authenticationError = ('invalidCredentials' == resolvedError),
                data = {
                    password: '',
                    rememberMe: false,
                    username: ''
                },
                logins = [],

                getAuthenticationError = function() {
                    return authenticationError;
                },

                getLogins = function() {
                    if (BUILD.INFO.BUILD_ENV !== 'dev') {
                        return logins;
                    }

                    if (logins.length == 0) {
                        logins.push({
                            name: 'manager3',
                            username: 'tepatel@redhat.com',
                            password: '12345324'
                        });
                        logins.push({
                            name: 'user1',
                            username: 'mperkins@redhat.com',
                            password: '12345324'
                        });
                        logins.push({
                            name: 'user2',
                            username: 'sbienkow@redhat.com',
                            password: '12345324'
                        });
                    }

                    return logins;
                },

                hasLogins = function() {
                    return (getLogins().length > 0);
                },

                login = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }

                    Auth.login({
                        username: data.username,
                        password: data.password,
                        rememberMe: data.rememberMe
                    }).then(function() {
                        authenticationError = false;
                        Principal.identity().then(function(account) {
                            $rootScope.$broadcast('onLoggedIn', {
                                account: account,
                                isAuthenticated: Principal.isAuthenticated()
                            });
                        });
                        if ($rootScope.previousStateName === 'activate' || $rootScope.previousStateName === 'login' || $rootScope.previousStateName === 'register') {
                            $state.go('home');
                        } else {
                            $rootScope.back();
                        }
                    }).catch(function() {
                        authenticationError = true;
                    });
                },

                setLogin = function(credentials) {
                    data.username = credentials.username;
                    data.password = credentials.password;

                    login();
                };

            $timeout(function() {
                angular.element('[ng-model="ctrl.data.username"]').focus();
            });

            var managerLogin = function() {
                var login = {
                    username: 'tom.manager@openlearningplatform.org',
                    password: '12345324'
                }
                setLogin(login);
            };

            var userLogin = function() {
                var login = {
                    username: 'tim.learner@openlearningplatform.org',
                    password: '12345324'
                }
                setLogin(login);
            };

            return {
                data: data,
                getAuthenticationError: getAuthenticationError,
                getLogins: getLogins,
                hasLogins: hasLogins,
                login: login,
                setLogin: setLogin,
                managerLogin: managerLogin,
                userLogin: userLogin
            };
        }
    );
})();
