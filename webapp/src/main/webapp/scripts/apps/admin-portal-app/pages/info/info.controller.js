/**
 * Node Info Controller.
 */

'use strict';

(function() {
    var module = angular.module('app.apps.admin-portal-app');

    module.controller('InfoController',
        function(resolvedData, BUILD, Principal) {
            var generalInfo = [],
                headerInfo = [],
                propertyInfo = [],

                getGeneralInfo = function() {
                    if (generalInfo.length == 0) {
                        generalInfo.push({
                            name: 'Build Date & Time',
                            value: BUILD.INFO.BUILD_DATE_TIME
                        });
                        generalInfo.push({
                            name: 'Build Environment',
                            value: BUILD.INFO.BUILD_ENV
                        });
                        generalInfo.push({
                            name: 'Build Version',
                            value: BUILD.INFO.BUILD_VERSION
                        });

                        if (resolvedData && resolvedData.generalInfo) {
                            if (resolvedData.more) {
                                angular.forEach(resolvedData.generalInfo, function(value, key) {
                                    generalInfo.push({
                                        name: key.replace(/\./, ' '),
                                        value: value
                                    });
                                });

                                generalInfo = generalInfo.sort(sortFunction);
                            } else {
                                generalInfo.push({
                                    name: 'Server Hostname',
                                    value: resolvedData.generalInfo['server.hostname']
                                });
                            }
                        }
                    }

                    return generalInfo;
                },

                getHeaderInfo = function() {
                    if (headerInfo.length == 0) {
                        angular.forEach(resolvedData.headerInfo, function(value, key) {
                            headerInfo.push({
                                name: key,
                                value: value
                            });
                        });

                        headerInfo = headerInfo.sort(sortFunction);
                    }

                    return headerInfo;
                },

                getPropertyInfo = function() {
                    if (propertyInfo.length == 0) {
                        angular.forEach(resolvedData.propertyInfo, function(value, key) {
                            propertyInfo.push({
                                name: key,
                                value: value
                            });
                        });

                        propertyInfo = propertyInfo.sort(sortFunction);
                    }

                    return propertyInfo;
                },

                sortFunction = function(a, b) {
                    return a.name == b.name ? 0 : +(a.name > b.name) || -1;
                },

                zend;

            return {
                getGeneralInfo: getGeneralInfo,
                getHeaderInfo: getHeaderInfo,
                getPropertyInfo: getPropertyInfo,
                more: resolvedData.more
            };
        }
    );
})();
