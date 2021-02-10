'use strict';

(function() {
    angular.module('app.components').factory('ConfigurationService', function($filter, $http) {
        return {
            get: function() {
                return $http.get('admin/manage/configprops').then(function(response) {
                    var orderBy = $filter('orderBy'),
                        properties = [];

                    angular.forEach(response.data, function(data) {
                        properties.push(data);
                    });

                    return orderBy(properties, 'prefix');
                });
            }
        };
    });
})();