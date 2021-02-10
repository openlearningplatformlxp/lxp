'use strict';

(function() {
    var module = angular.module('app.components');

    module.filter('completedate', function() {
        return function(completedate) {
            if (!completedate) {
                return '';
            }

            var value = completedate.toString().trim().replace(/^\+/, '');

            if (value.match(/[^0-9]/)) {
                return completedate;
            }

            var month, day, year;

            switch (value.length) {
                case 1:
                case 2:
                    month = value;
                    break;
                case 3:
                    month = value.slice(0, 2);
                    day = value.slice(2);
                case 4:
                    month = value.slice(0, 2);
                    day = value.slice(2);
                default:
                    month = value.slice(0, 2);
                    day = value.slice(2, 4);
                    year = value.slice(4);
            }

            if (year) {
                return month + "/" + day + "/" + year;
            } else if (day) {
                return month + "/" + day;
            } else {
                return month;
            }
        };
    });
})();
