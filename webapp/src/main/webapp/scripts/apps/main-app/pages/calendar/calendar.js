'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.config(function($stateProvider) {
        $stateProvider.state('calendar', {
            parent: 'site',
            url: '/calendar',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    controller: 'CalendarController',
                    controllerAs: 'ctrl',

                    templateUrl: 'scripts/apps/main-app/pages/calendar/calendar.html'
                }
            },
            resolve: {
                mainTranslatePartialLoader: function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }
            }
        });
    });
})();
