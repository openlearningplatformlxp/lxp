'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('MetricsController',
        function($translate, AlertsService, MonitoringService) {
            var data = {
                    cachesStats: {},
                    servicesStats: {}
                },
                metrics = {},
                updatingMetrics = true,

                getMetrics = function() {
                    return metrics;
                },

                init = function() {
                    refresh();
                },

                isUpdatingMetrics = function() {
                    return updatingMetrics;
                },

                refresh = function() {
                    updatingMetrics = true;
                    MonitoringService.getMetrics().then(function(promise) {
                        metrics = promise;
                        updateMetrics(metrics);
                        updatingMetrics = false;
                    }, function(promise) {
                        metrics = promise.data;
                        updateMetrics(metrics);
                        updatingMetrics = false;
                    });
                },

                refreshThreadDumpData = function() {
                    MonitoringService.threadDump().then(function(results) {
                        AlertsService.alert({
                            button: {
                                text: $translate.instant('global.buttons.Close')
                            },
                            include: {
                                controller: 'MetricsModalController as ctrl',
                                resolve: {
                                    threadDump: function() {
                                        return results;
                                    }
                                },
                                templateUrl: 'scripts/apps/admin-portal-app/pages/metrics/metrics.modal.html'
                            },
                            size: 'xlarge',
                            title: '&nbsp; <span class="glyphicon glyphicon-dashboard"></span> ' + $translate.instant('metrics.jvm.threads.dump.title')
                        });
                    });
                },

                updateMetrics = function(newValue) {
                    data.servicesStats = {};
                    data.cachesStats = {};

                    angular.forEach(newValue.timers, function(value, key) {
                        if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1) {
                            data.servicesStats[key] = value;
                        }

                        if (key.indexOf('net.sf.ehcache.Cache') !== -1) {
                            // remove gets or puts
                            var index = key.lastIndexOf('.'),
                                newKey = key.substr(0, index);

                            // Keep the name of the domain
                            index = newKey.lastIndexOf('.');
                            data.cachesStats[newKey] = {
                                'name': newKey.substr(index + 1),
                                'value': value
                            };
                        }
                    });
                };

            init();

            return {
                data: data,
                getMetrics: getMetrics,
                isUpdatingMetrics: isUpdatingMetrics,
                refresh: refresh,
                refreshThreadDumpData: refreshThreadDumpData
            };
        }
    );
})();
