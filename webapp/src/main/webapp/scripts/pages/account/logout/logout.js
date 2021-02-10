'use strict';

(function() {
    var module = angular.module('app.pages');

    module.config(function($stateProvider) {
        $stateProvider.state('logout', {
            parent: 'account',
            url: 'api/logout',
            data: {
                authorities: []
            },
            views: {
                'content@site': {
                    template: '<i class="fa fa-spinner fa-spin"></i> Logging out...',
                    controller: function($state, $timeout, Auth) {
                        $timeout(function() {
                            Auth.logout();

                            // TODO: SAC: the $timeout is a hack to get logout to work properly - fix this.

                            $timeout(function() {
                                $state.go('home', {}, {
                                    reload: true
                                });
                            }, 100);
                        }, 100);
                    }
                }
            }
        });
    });
})();