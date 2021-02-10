'use strict';

(function() {
    var module = angular.module('app.components'),
        controllerAs = 'ctrl',
        controllerName = 'TextEditorController',
        templateUrl = 'scripts/components/code-editor/code-editor.html';

    module.service('TextEditorModal',
        function($rootScope, $translate, AlertsService) {
            var showTextEditor = function(options) {
                AlertsService.confirm({
                    buttonOk: options.buttonOk,
                    include: {
                        controller: controllerName + ' as ' + controllerAs,
                        resolve: {
                            resolvedContent: function() {
                                return options.content;
                            }
                        },
                        templateUrl: templateUrl
                    },
                    size: '100%',
                    title: options.title
                });
            };

            return {
                showTextEditor: function(options) {
                    return new showTextEditor(options);
                }
            }
        }
    );
})();
