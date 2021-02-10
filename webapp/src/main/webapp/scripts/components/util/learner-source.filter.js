'use strict';

(function() {
    angular.module('app.components').filter('learnerSource', function() {
        return function(input) {
            if (!!input) {
                if (input === 'KALTURA') {
                    return 'Kaltura'
                } else if (input === 'LYNDA') {
                    return 'Linkedin.com';
                } else if (input === 'ALLEGO') {
                    return 'Allego';
                }
                return input;
            } else {
                return '';
            }
        }
    });


})();