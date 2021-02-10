/**
 * Geolocation service.
 *
 * Service to get the client location (i.e latitude and longitude), if available and allowed.
 * See http://www.w3schools.com/html/html5_geolocation.asp
 *
 * Example:
 *     GeolocationService.getCurrentPosition().then(
 *         function success(response) {
 *             latitude = response.coords.latitude;
 *             longitude = response.coords.longitude;
 *         },
 *         function error() {
 *             // could not get location - user may have denied request or not supported
 *         }
 *     );
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('GeolocationService', function($http, $q, $window) {
        var currentLocation = undefined,

            getCurrentPosition = function() {
                var deferred = $q.defer();

                if ($window.navigator.geolocation) {
                    $window.navigator.geolocation.getCurrentPosition(
                        function success(position) {
                            currentLocation = position;

                            deferred.resolve(currentLocation);
                        },
                        function error(response) {
                            deferred.reject(response);
                        }
                    );
                } else {
                    deferred.reject('Geolocation not supported.');
                }

                return deferred.promise;
            };

        return {
            getCurrentPosition: function(reload) {
                if (reload || !currentLocation) {
                    currentLocation = undefined;

                    return getCurrentPosition();
                }

                var deferred = $q.defer();

                deferred.resolve(currentLocation);

                return deferred.promise;
            }
        }
    });
})();
