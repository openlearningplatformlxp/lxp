'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CatalogCourseTotaraController',
        function($state, $stateParams, $sce, localStorageService) {
            var data = {};

            var init = function() {
                    data.lpUrl = "https://" + window.location.hostname + "/catalog/learning-path/" + $stateParams.programId;
                    data.url = $stateParams.externalUrl || (appGlobal.config.get('totara.course.baseurl') + $stateParams.courseId + "%26xl_lp=https://" + window.location.hostname + "/catalog/learning-path/" + $stateParams.programId);
                },

                getIframeSrc = function() {
                    return $sce.trustAsResourceUrl(data.url);
                },

                navigateBack = function() {

                    if ($stateParams.source === 'learning-path') {
                        $state.go("learning-path", {
                            id: $stateParams.programId,
                            personal: $stateParams.personal
                        });
                    } else if ($stateParams.source === 'search') {
                        $state.go("search", {});
                    } else if ($stateParams.source === 'home') {
                        $state.go("home", {});
                    } else if ($stateParams.source === 'profile') {
                        $state.go("profile-main", {});
                    } else if ($stateParams.source === 'calendar') {
                        $state.go("calendar", {});

                    } else if (!$stateParams.source) {
                        window.history.back();
                    }
                };

            init();

            var buttonReturnLabel = 'Return to Path';
            if ($stateParams.source == null) {
                $stateParams.source = localStorageService.get('catalog-totara-source');
            }
            localStorageService.set('catalog-totara-source', $stateParams.source);

            if ($stateParams.source === 'learning-path') {
                buttonReturnLabel = 'Return to Path';
            } else if ($stateParams.source === 'search') {
                buttonReturnLabel = 'Return to search results';
            } else if ($stateParams.source === 'home') {
                buttonReturnLabel = 'Return to Home';
            } else if ($stateParams.source === 'profile') {
                buttonReturnLabel = 'Return to Profile';
            } else if ($stateParams.source === 'calendar') {
                buttonReturnLabel = 'Return to Calendar';
            } else if (!$stateParams.source) {
                // Matt, try to fix edge case
                // In code above, the default nav action is history back, so provide a label so it shows
                buttonReturnLabel = 'Return to the RHU';
            }
            return {
                data: data,
                getIframeSrc: getIframeSrc,
                navigateBack: navigateBack,
                buttonReturnLabel: buttonReturnLabel
            };
        }
    );


})();
