'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('LanguageController',
        function($translate, Language, tmhDynamicLocale) {
            var languages = [],

                changeLanguage = function(languageKey) {
                    $translate.use(languageKey);
                    tmhDynamicLocale.set(languageKey);
                },

                getLanguages = function() {
                    return languages;
                },

                init = function() {
                    Language.getAll().then(function(data) {
                        languages = data;
                    });
                };

            init();

            return {
                changeLanguage: changeLanguage,
                getLanguages: getLanguages
            };
        }
    );

    module.filter('findLanguageFromKey', function() {
        return function(lang) {
            return {
                "en": "English",
                "fr": "Français"
            } [lang];
        }
    });
})();