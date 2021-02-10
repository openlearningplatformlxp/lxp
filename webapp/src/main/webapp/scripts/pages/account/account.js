'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('account', {
            abstract: true,
            parent: 'site'
        });
    });
})();