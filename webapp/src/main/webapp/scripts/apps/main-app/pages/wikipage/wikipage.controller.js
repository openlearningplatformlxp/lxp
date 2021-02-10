'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('WikipageController',
        function($state, $sce, $http, resolvedWikipage) {
            var data = {
                wikipage: resolvedWikipage,
                trustedWikipageHtml: null
            };

            var init = function() {
                data.trustedWikipageHtml = $sce.trustAsHtml(data.wikipage.htmlContent);
            };

            init();

            return {
                data: data
            };
        }
    );

})();
