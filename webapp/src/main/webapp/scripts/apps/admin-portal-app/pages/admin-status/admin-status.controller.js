'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('AdminStatusController',
        function(resolvedPage) {
            var data = {
                    errors: [],
                    hasErrorsOrWarnings: false,
                    warnings: []
                },

                init = function() {
                    addError(!!resolvedPage.emailFailedCount, 'fa-envelope-o', 'Failed Emails', resolvedPage.emailFailedCount);
                    addWarning(!!resolvedPage.emailPendingCount, 'fa-envelope-o', 'Pending Failed Emails', resolvedPage.emailPendingCount);

                    data.hasErrorsOrWarnings = ((data.errors.length > 0) || (data.warnings.length > 0));
                },

                addError = function(isError, iconClass, label, value) {
                    if (isError) {
                        data.errors.push({
                            iconClass: iconClass,
                            label: label,
                            value: value
                        });
                    }
                },

                addWarning = function(isWarning, iconClass, label, value) {
                    if (isWarning) {
                        data.warnings.push({
                            iconClass: iconClass,
                            label: label,
                            value: value
                        });
                    }
                },

                zend;

            init();

            return {
                data: data
            };
        }
    );
})();
