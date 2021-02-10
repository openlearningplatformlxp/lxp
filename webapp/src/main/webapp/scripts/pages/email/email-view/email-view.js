'use strict';

(function() {
    var module = angular.module('app.pages'),
        controllerAs = 'ctrl',
        controllerName = 'ViewEmailController',
        templateUrl = 'scripts/pages/email/email-view/email-view.html';

    appGlobal.translate.addPart('email-view');

    module.config(function($stateProvider) {
        $stateProvider.state('email-view', {
            parent: 'site',
            url: '/email-view/{emailId:int}',
            data: {
                pageTitle: 'Email',
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@site': {
                    controller: controllerName,
                    controllerAs: controllerAs,
                    templateUrl: templateUrl,
                    resolve: {
                        resolvedEmail: function($http, $stateParams) {
                            return $http.get('api/admin/email/' + $stateParams.emailId).then(function(response) {
                                return response.data;
                            }, function(response) {
                                return null;
                            });
                        }
                    }
                }
            }
        });
    });

    module.service('ViewEmailModal',
        function($rootScope, $translate, AlertsService) {
            var showEmail = function(options) {
                AlertsService.alert({
                    button: {
                        text: $translate.instant('global.buttons.Close')
                    },
                    include: {
                        controller: controllerName + ' as ' + controllerAs,
                        resolve: {
                            resolvedEmail: function() {
                                return options.email;
                            }
                        },
                        templateUrl: templateUrl
                    },
                    size: 'xxlarge',
                    title: '&nbsp; <i class="fa fa-envelope-o"></i> ' + $translate.instant('email-view.title')
                });
            };

            return {
                showEmail: function(options) {
                    return new showEmail(options);
                }
            }
        }
    );
})();
