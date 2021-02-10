'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('ResourceActivityController',
        function($scope, $sce, $timeout, $location, $http, AlertsService) {

            var data = {
                    videoSaveInterval: 10,
                    activity: {},
                    type: null,
                    videoTime: {
                        current: null,
                        total: null,
                        lastSaveValue: null
                    }
                },

                init = function() {

                    $scope.triggerCompleteOnView();

                    data.activity = $scope.activityData;
                    data.nextActivity = $scope.nextActivityData;

                    //figure out what resource type based on extension
                    var fileExt = data.activity.content.url.split('.').pop();

                    if (fileExt == 'pdf') {
                        data.type = "PDF";
                        data.typeDisplayName = "Reading";
                        data.pdfUrl = getUrl();
                    }

                    if (fileExt == 'mp4') {
                        data.type = "VIDEO";
                        data.typeDisplayName = "Video clip";
                        data.videoConfig = {
                            preload: "none",
                            sources: [{
                                src: getVideoUrl(),
                                type: 'video/mp4;codecs="avc1.42E01E, mp4a.40.2"'
                            }],
                            theme: {
                                url: "bower_components/videogular-themes-default/videogular.css"
                            },
                            plugins: {
                                controls: {
                                    autoHide: true,
                                    autoHideTime: 5000
                                }
                            }
                        }

                        var resumeVideo = function() {
                            data.API.seekTime(data.activity.content.videoTime, false);
                        };


                        if (data.activity.content.videoTime && data.activity.content.videoTime > 0) {
                            AlertsService.confirm({
                                buttonOk: {
                                    text: "Resume Video",
                                    onClick: resumeVideo
                                },
                                text: "Would you like to resume the video where you left?<br><br>",
                                title: "Resume Video"
                            })

                        }
                    }
                },

                getUrl = function() {
                    if (location.hostname === "localhost" || location.hostname === "127.0.0.1") {
                        return $sce.trustAsResourceUrl("asset-storage/asset/" + data.activity.content.url + "#view=fit");
                    } else {
                        return $sce.trustAsResourceUrl($location.protocol() + "://docs.google.com/gview?url=" + data.activity.content.url + "&embedded=true");
                    }
                };

            var getVideoUrl = function() {
                if (location.hostname === "localhost" || location.hostname === "127.0.0.1") {

                    var s = angular.copy(data.activity.content.url);
                    var prefix = 'http://';
                    if (s.substr(0, prefix.length) !== prefix) {
                        s = prefix + s;
                    }

                    return $sce.trustAsResourceUrl(s);
                } else {
                    return $sce.trustAsResourceUrl(data.activity.content.url);
                }
            };

            var playerOnUpdateTime = function($currentTime, $duration) {
                data.videoTime.current = $currentTime;
                data.videoTime.total = $duration;
                if ($currentTime != null) {
                    var floorTime = Math.floor($currentTime);
                    if (floorTime % data.videoSaveInterval == 0) {
                        if (floorTime != data.videoTime.lastSaveTime && floorTime != 0) {
                            saveVideoTime();
                            data.videoTime.lastSaveTime = Math.floor($currentTime);
                        }
                    }
                }
            };

            var playerOnReady = function(API) {
                data.API = API;
            };

            var playerOnComplete = function() {

                if (data.videoTime.total == data.activity.content.videoTime) {
                    data.activity.content.videoTime = 0;
                    data.API.seekTime(0, false);
                    return;
                }
                saveVideoTime();
            };

            var timePointsObject = {
                timeLapse: {
                    start: 0,
                    end: 0
                },
                onLeave: function onLeave(currentTime, timeLapse, params) {

                    //save video time
                    saveVideoTime();
                },
                onUpdate: function onUpdate(currentTime, timeLapse, params) {
                },
                onComplete: function onComplete(currentTime, timeLapse, params) {
                }
            };

            var timePoints = [timePointsObject];

            var saveVideoTime = function() {
                //save video time
                var postBody = {
                    courseId: data.activity.courseId,
                    activityId: data.activity.id,
                    videoTime: data.videoTime.current
                };

                $http.post("api/courseplayer/videotime", postBody).then(function(response) {

                });
            };

            init();

            var getVideoConfig = {
                preload: "none",
                sources: [{
                    src: getVideoUrl(),
                    type: 'video/mp4;codecs="avc1.42E01E, mp4a.40.2"'
                }],
                theme: {
                    url: "bower_components/videogular-themes-default/videogular.css"
                },
                plugins: {
                    controls: {
                        autoHide: true,
                        autoHideTime: 5000
                    }
                },
                cuePoints: {
                    timePoint: timePoints
                }
            };

            return {
                data: data,
                goToNextActivity: $scope.goToNextActivity,
                getUrl: getUrl,
                sce: $sce,
                getVideoConfig: getVideoConfig,
                playerOnUpdateTime: playerOnUpdateTime,
                playerOnComplete: playerOnComplete,
                playerOnReady: playerOnReady
            };
        }
    );
})();
