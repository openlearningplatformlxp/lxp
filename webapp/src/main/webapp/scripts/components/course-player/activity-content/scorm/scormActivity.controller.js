'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('ScormActivityController',
        function($scope, $sce, $timeout, $location, $http, $rootScope, ScormValueService) {

            var data = {

                },

                init = function() {

                    $scope.triggerCompleteOnView();

                    data.activity = $scope.activityData;
                    data.nextActivity = $scope.nextActivityData;
                    data.content = $scope.activityData.content;
                    data.resourceUrl = getUrl(data.content.url);
                    data.debuggingEnabled = false;

                    //hard code this value into the scorm config
                    data.content.values["cmi.core.lesson_mode"] = "normal";

                    data.isComplete = (data.content.values["cmi.core.lesson_status"] == "passed" || data.content.values["cmi.core.lesson_status"] == "completed") || data.content.status == "COMPLETE";

                    window.DoCPExit = function() {
                        $scope.goToNextActivity();
                    };

                    window.API = {
                        LMSInitialize: function(param1, param2, param3, param4) {
                            if (data.debuggingEnabled) {
                                console.log("***********START***********");
                            }
                            return true;
                        },
                        LMSFinish: function(param1, param2, param3, param4) {
                            if (data.debuggingEnabled) {
                                console.log("FINISH");
                            }
                        },
                        LMSGetValue: function(key) {
                            if (data.debuggingEnabled) {
                                console.log("******** GET VALUE *******");
                                console.log(key);
                                console.log(data.content.values[key] || "NULL");
                            }
                            return data.content.values[key] || '';
                        },
                        LMSSetValue: function(key, value) {
                            if (data.debuggingEnabled) {
                                console.log("******** SET VALUE *******");
                                console.log(key);
                                console.log(value || "NULL");
                            }
                            data.content.values[key] = value;

                            // TODO: (WJK) Revise this to manage more than one value at a time, since this involves a LOT of calls.
                            ScormValueService.setScormValue({
                                activityId: data.activity.id,
                                key: key,
                                value: value,
                                allowCompletion: !data.activity.allowsManualCompletion
                            }).then(function success(response) {
                                if (response.isCompleted) {
                                    data.isComplete = true;
                                }
                            }, function(error) {
                                console.log(error);
                            });
                        },
                        LMSCommit: function() {
                            if (data.debuggingEnabled) {
                                console.log("---- COMMIT -----");
                            }
                            return '';
                        },
                        LMSGetLastError: function() {
                            if (data.debuggingEnabled) {
                                console.log("----- LAST ERROR -----");
                            }
                            return 0;
                        },
                        LMSGetErrorString: function() {
                            if (data.debuggingEnabled) {
                                console.log("---- ERROR STRING -----");
                            }
                            return '';
                        },
                        LMSGetDiagnostic: function() {
                            if (data.debuggingEnabled) {
                                console.log("----- DIAGNOSTIC ------");
                            }
                            return '';
                        }
                    };
                },

                getUrl = function(resourceUrl) {
                    if (resourceUrl.indexOf("/totara/") !== -1) {
                        try {
                            return resourceUrl.substring(resourceUrl.indexOf("/totara/"));
                        } catch (error) {
                            // Noop: Allow to fall through to default handling
                        }
                    }

                    return formatAssetStorageUrl(resourceUrl);
                },

                formatAssetStorageUrl = function(resourceUrl) {
                    var i,
                        len,
                        protocols = ['http://', 'https://'];
                    for (i = 0, len = protocols.length; i < len; i += 1) {
                        if (resourceUrl.startsWith(protocols[i])) {
                            resourceUrl = resourceUrl.substring(protocols[i].length);
                            break;
                        }
                    }
                    return $sce.trustAsResourceUrl("asset-storage/asset/" + resourceUrl);
                },

                zend;

            init();

            return {
                data: data,
                goToNextActivity: $scope.goToNextActivity
            }
        }
    );
})();