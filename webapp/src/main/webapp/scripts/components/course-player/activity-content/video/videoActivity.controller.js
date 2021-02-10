'use strict';

(function() {
    var module = angular.module('app.components');

    module.controller('VideoActivityController',
        function($scope, $sce) {

            var data = {},

                init = function() {

                    $scope.triggerCompleteOnView();

                    data.content = $scope.activityData.content;

                    data.videoConfig = {
                        preload: "none",
                        sources: [{
                            src: $sce.trustAsResourceUrl($scope.activityData.content.url),
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
                },

                zend;

            init();

            return {
                data: data
            };
        }
    );
})();
