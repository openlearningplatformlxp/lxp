'use strict';

var appGlobal;

(function() {
    appGlobal = {
        commonRequires: function() {
            var commonRequires = [
                    'angular-google-analytics',
                    'app.components',
                    'app.services',
                    'app.pages',
                    'blockUI',
                    'cfp.loadingBar',
                    'LocalStorageModule',
                    'tmh.dynamicLocale',
                    'ngAria',
                    'ngclipboard',
                    'ngResource',
                    'ngSanitize',
                    'ui.bootstrap',
                    'ui.router',
                    'ui.carousel',
                    'ui.checkbox',
                    'vModal',
                    'ngCookies',
                    'pascalprecht.translate',
                    'ngCacheBuster',
                    'angularFileUpload',
                    'reduxl.common',
                    'infinite-scroll',
                    'LocalStorageModule',
                    'angularMoment',
                    'angucomplete-alt',
                    'ngFileUpload',
                    'com.2fdevs.videogular',
                    'com.2fdevs.videogular.plugins.controls',
                    'dndLists',
                    'textAngular',
                    'ngQuill'
                ],

                get = function() {
                    return commonRequires;
                };

            return {
                get: get
            };
        }(),
        config: function() {
            var config = {},

                get = function(key) {
                    return config[key];
                },

                has = function(key) {
                    return (config[key] && config[key].length > 0);
                },

                set = function(key, value) {
                    config[key] = value;
                },

                setAll = function(conf) {
                    config = conf;
                };

            return {
                get: get,
                has: has,
                set: set,
                setAll: setAll
            };
        }(),
        translate: function() {
            var parts = [],

                addPart = function(part) {
                    parts.push(part);
                },

                getParts = function() {
                    return parts;
                };

            return {
                addPart: addPart,
                getParts: getParts
            };
        }()
    };
})();
