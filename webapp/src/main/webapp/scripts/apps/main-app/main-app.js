'use strict';

(function() {
    var PAGE_TITLE_DEFAULT = 'Red Hat University';

    var module = angular.module('app.apps.main-app', appGlobal.commonRequires.get());

    module.run(function($rootScope, $location, $http, $state, $timeout, $translate, $anchorScroll, ActivityLogout,
                        Analytics, Auth, blockUI, cfpLoadingBar, Principal, Language, WALKME) {

        // Analytics must be injected above (even though seemingly not used) for auto-analytics to work.

        $rootScope.pageTitle = PAGE_TITLE_DEFAULT;

        // NOTE: (WJK) Adding in order to force views to scroll back to the top on route changes.
        $rootScope.$on("$stateChangeSuccess", function () {
            $timeout(function() {
                $anchorScroll();
            });
        });

        $rootScope.makeScript = function (url) {
            var script = document.createElement('script');
            script.setAttribute('src', url);
            script.setAttribute('type', 'text/javascript');
            document.getElementsByTagName('body')[0].appendChild(script);
        };

        var url = WALKME.SCRIPT;
        $rootScope.makeScript(url);

        var pageBlockEnabled = true,
            pageBlockBarPromise = false,
            pageBlockMessagePromise = false,
            pageBlockProtectionPromise = false,
            pageBlock = {
                start: function() {
                    if (pageBlockProtectionPromise) {
                        return;
                    }

                    blockUI.start('-');

                    pageBlockBarPromise = $timeout(function() {
                        cfpLoadingBar.start();
                        pageBlockBarPromise = false;
                    }, 200);

                    pageBlockMessagePromise = $timeout(function() {
                        blockUI.message('Loading...');
                        pageBlockMessagePromise = false;
                    }, 500);

                    pageBlockProtectionPromise = $timeout(function() {
                        pageBlock.end();
                        pageBlockProtectionPromise = false;
                    }, 3000); // only show bar for max of 3 seconds... just in case... to prevent permanently blocked pages!
                },
                end: function() {
                    if (!pageBlockProtectionPromise) {
                        return;
                    }

                    $timeout.cancel(pageBlockProtectionPromise);
                    pageBlockProtectionPromise = false;

                    blockUI.stop();

                    if (pageBlockBarPromise) {
                        $timeout.cancel(pageBlockBarPromise);
                        pageBlockBarPromise = false;
                    } else {
                        cfpLoadingBar.complete();
                    }

                    if (pageBlockMessagePromise) {
                        $timeout.cancel(pageBlockMessagePromise);
                        pageBlockMessagePromise = false;
                    }
                }
            };

        var markPageLoaded = function(ms) {
                if (!$rootScope.initialPageLoaded) {
                    $timeout(function() {
                        $rootScope.initialPageLoaded = true;
                    }, ms);
                }
            };

        markPageLoaded(5000);

        $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
            if (pageBlockEnabled) {
                pageBlock.start();
            }

            if (toState.data.pageDisabled === true || (angular.isFunction(toState.data.pageDisabled) && toState.data.pageDisabled() === true)) {
                event.preventDefault();
                $state.go('home');

                return;
            }

            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize(true);
            }

            // Update the language
            Language.getCurrent().then(function(language) {
                $translate.use(language);
            });
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            if (pageBlockEnabled) {
                pageBlock.end();
            }

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            if (toState && toState.data && angular.isString(toState.data.pageTitle)) {
                $rootScope.pageTitle = toState.data.pageTitle;
            } else if (toState && toState.data && angular.isFunction(toState.data.pageTitle)) {
                $rootScope.pageTitle = toState.data.pageTitle();
            } else {
                $rootScope.pageTitle = PAGE_TITLE_DEFAULT;
            }

            markPageLoaded(500);
        });

        $rootScope.$on('$stateChangeError',  function(event, toState, toParams, fromState, fromParams, error) {
            if (pageBlockEnabled) {
                pageBlock.end();
            }

            markPageLoaded(500);
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };

        ActivityLogout.start({
            idleSecsBeforeWarning: appGlobal.config.get('session.timeout.idleSecsBeforeWarning'),
            secsBeforeLogout: appGlobal.config.get('session.timeout.secsBeforeLogout')
        });
    });

    module.config(function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, AnalyticsProvider, blockUIConfig, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {
        // enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'content-container@': {
                    templateUrl: 'scripts/components/content-containers/full-page/full-page.html',
                    controller: 'FullPageController',
                    controllerAs: 'ctrl'
                },
                'header@site': {
                    templateUrl: 'scripts/components/headers/header-main/header-main.html',
                    controller: 'HeaderMainController',
                    controllerAs: 'ctrl'
                },
                'footer@site': {
                    templateUrl: 'scripts/components/footers/footer-main/footer-main.html',
                    controller: 'FooterMainController',
                    controllerAs: 'ctrl'
                }
            },
            resolve: {
                authorize: function(Auth) {
                    return Auth.authorize();
                },
                translatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');

                    angular.forEach(appGlobal.translate.getParts(), function(part) {
                        $translatePartialLoader.addPart(part);
                    });

                    $translate.refresh();
                },
                resolvedAccountInfo: function(Principal) {
                    return Principal.identity().then(function(account) {
                        return {
                            account: account,
                            isAuthenticated: Principal.isAuthenticated()
                        };
                    });
                }
            }
        });

        $httpProvider.interceptors.push('authExpiredInterceptor');

        // TODO: SAC: remove this? $httpProvider.interceptors.push('notificationInterceptor');

        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js'); // TODO: SAC: What is this?
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

        if (appGlobal.config.has('googleAnalytics.trackingId')) {
            AnalyticsProvider.setAccount(appGlobal.config.get('googleAnalytics.trackingId'));
            AnalyticsProvider.setDomainName(appGlobal.config.get('googleAnalytics.domainName'));
            AnalyticsProvider.setPageEvent('$stateChangeSuccess');
        } else {
            AnalyticsProvider.delayScriptTag(true);
        }

        //remove # from url
        if(!(location.hostname === "localhost" || location.hostname === "127.0.0.1")){
            $locationProvider.html5Mode(true);
        }

        blockUIConfig.autoBlock = false;
        blockUIConfig.template = '<div ng-if="state.message != \'-\'" class="block-ui-message-container" aria-atomic="true" aria-live="assertive"><div class="block-ui-message" ng-class="$_blockUiMessageClass" ng-bind="state.message"></div></div>';
    });
})();


// Style on scroll from https://pqina.nl/blog/applying-styles-based-on-the-user-scroll-position-with-smart-css/
// Adds data-scroll to top HTML element
var debounce = function(fn) {
    var frame;
    return function(params) {
        if (frame) {
            cancelAnimationFrame(frame);
        }
        frame = requestAnimationFrame(function() {
            fn(params);
        });
    }
};

var elHasClass = function(el, cls) {
    if(!el) return;
    el.classList.contains(cls);
};

var elAddClass = function(el, cls) {
    if(!el) return;
    if(!elHasClass(el, cls)) {
        el.classList.add(cls);
    }
};

var elRemoveClass = function(el, cls) {
    if(!el) return;
    if(elHasClass(el, cls)) {
        el.classList.remove(cls);
    }
};

var storeScroll = function() {
    var scroll = window.scrollY;
    document.documentElement.dataset.scroll = scroll;
    // 55px is the height of the header element
    if(scroll > 55) {
        elAddClass(document.getElementsByClassName('site-header')[0], 'site-header-scrolled');
    } else {
        elRemoveClass(document.getElementsByClassName('site-header')[0], 'site-header-scrolled');
    }
};
document.addEventListener('scroll', debounce(storeScroll), { passive: true });

storeScroll();
