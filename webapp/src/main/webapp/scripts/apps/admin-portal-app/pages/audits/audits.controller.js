'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AuditsController',
        function($filter, AuditsService) {
            var audits = undefined,
                data = {
                    fromDate: undefined,
                    toDate: undefined
                },

                getAudits = function() {
                    return audits;
                },

                init = function() {
                    today();
                    previousMonth();
                    onChangeDate();
                },

                onChangeDate = function() {
                    var dateFormat = 'yyyy-MM-dd',
                        fromDate = $filter('date')(data.fromDate, dateFormat),
                        toDate = $filter('date')(data.toDate, dateFormat);

                    AuditsService.findByDates(fromDate, toDate).then(function(data) {
                        audits = data;
                    });
                },

                previousMonth = function() {
                    var fromDate = new Date();

                    if (fromDate.getMonth() === 0) {
                        fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
                    } else {
                        fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
                    }

                    data.fromDate = fromDate;
                },

                // Date picker configuration
                today = function() {
                    // Today + 1 day - needed if the current day must be included
                    var today = new Date();
                    data.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
                };

            init();

            return {
                data: data,
                getAudits: getAudits,
                onChangeDate: onChangeDate
            };
        }
    );
})();
