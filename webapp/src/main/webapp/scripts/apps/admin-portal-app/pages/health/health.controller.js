'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('HealthController',
        function($translate, AlertsService, MonitoringService) {
            var healthData = undefined,
                separator = '.',

                getHealthData = function() {
                    return healthData;
                },

                getLabelClass = function(statusState) {
                    if (statusState === 'UP') {
                        return 'label-success';
                    } else {
                        return 'label-danger';
                    }
                },

                refresh = function() {
                    MonitoringService.checkHealth().then(function(response) {
                        healthData = transformHealthData(response);
                    }, function(response) {
                        healthData = transformHealthData(response.data);
                    });
                },

                transformHealthData = function(data) {
                    var response = [];
                    flattenHealthData(response, null, data);
                    return response;
                },

                flattenHealthData = function(result, path, data) {
                    angular.forEach(data, function(value, key) {
                        if (isHealthObject(value)) {
                            if (hasSubSystem(value)) {
                                addHealthObject(result, false, value, getModuleName(path, key));
                                flattenHealthData(result, getModuleName(path, key), value);
                            } else {
                                addHealthObject(result, true, value, getModuleName(path, key));
                            }
                        }
                    });
                    return result;
                },

                getModuleName = function(path, name) {
                    var result;
                    if (path && name) {
                        result = path + separator + name;
                    } else if (path) {
                        result = path;
                    } else if (name) {
                        result = name;
                    } else {
                        result = '';
                    }
                    return result;
                },

                showHealth = function(health) {
                    var title = $translate.instant('health.indicator.' + baseName(health.name)),
                        subTitle = subSystemName(health.name);

                    if (subTitle) {
                        title += ': ' + subTitle;
                    }

                    AlertsService.alert({
                        button: {
                            text: $translate.instant('global.buttons.Close')
                        },
                        include: {
                            controller: 'HealthModalController as ctrl',
                            resolve: {
                                resolvedCurrentHealth: function() {
                                    return health;
                                }
                            },
                            templateUrl: 'scripts/apps/admin-portal-app/pages/health/health.modal.html'
                        },
                        size: 'xlarge',
                        title: '&nbsp; <span class="glyphicon glyphicon-heart"></span> ' + title
                    });
                },

                addHealthObject = function(result, isLeaf, healthObject, name) {

                    var healthData = {
                        'name': name
                    };
                    var details = {};
                    var hasDetails = false;

                    angular.forEach(healthObject, function(value, key) {
                        if (key === 'status' || key === 'error') {
                            healthData[key] = value;
                        } else {
                            if (!isHealthObject(value)) {
                                details[key] = value;
                                hasDetails = true;
                            }
                        }
                    });

                    // Add the of the details
                    if (hasDetails) {
                        angular.extend(healthData, {
                            'details': details
                        });
                    }

                    // Only add nodes if they provide additional information
                    if (isLeaf || hasDetails || healthData.error) {
                        result.push(healthData);
                    }
                    return healthData;
                },

                hasSubSystem = function(healthObject) {
                    var result = false;
                    angular.forEach(healthObject, function(value) {
                        if (value && value.status) {
                            result = true;
                        }
                    });
                    return result;
                },

                isHealthObject = function(healthObject) {
                    var result = false;
                    angular.forEach(healthObject, function(value, key) {
                        if (key === 'status') {
                            result = true;
                        }
                    });
                    return result;
                },

                baseName = function(name) {
                    if (name) {
                        var split = name.split('.');
                        return split[0];
                    }
                },

                subSystemName = function(name) {
                    if (name) {
                        var split = name.split('.');
                        split.splice(0, 1);
                        var remainder = split.join('.');
                        return remainder ? ' - ' + remainder : '';
                    }
                };

            refresh();

            return {
                baseName: baseName,
                getHealthData: getHealthData,
                getLabelClass: getLabelClass,
                refresh: refresh,
                showHealth: showHealth,
                subSystemName: subSystemName
            };
        }
    );
})();
