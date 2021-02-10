/**
 * IFrame Resizer - Resize iframe based on the size of its content that is dynamically pumped in.
 * Example:
 *
 *     In a controller:
 *
 *         iframeResizer = IframeResizerService.getIframeResizerInstance({
 *             content: email.plainText,
 *             contentType: 'text'
 *         })
 *
 *     In a template:
 *
 *         <iframe iframe-resizer="ctrl.iframeResizer"></iframe>
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('IframeResizerService', function($http) {
        var iframeResizer = function(options) {
            var opts = angular.copy(options) || {},

                getOptions = function() {
                    return opts;
                },

                init = function() {
                    opts.checkOrigin = angular.isDefined(opts.checkOrigin) ? opts.checkOrigin : false;
                    opts.heightCalculationMethod = opts.heightCalculationMethod || 'lowestElement';
                };

            init();

            return {
                getOptions: getOptions
            };
        };

        return {
            getIframeResizerInstance: function(options) {
                return new iframeResizer(options);
            }
        }
    });

    module.directive('iframeResizer',
        function() {
            return {
                link: function(scope, element) {
                    var hasContent = scope.iframeResizer.getOptions().content;

                    if (hasContent) {
                        var iframe = element[0],
                            iframedoc = iframe.document;

                        if (iframe.contentDocument) {
                            iframedoc = iframe.contentDocument;
                        } else if (iframe.contentWindow) {
                            iframedoc = iframe.contentWindow.document;
                        }

                        if (iframedoc) {
                            iframedoc.open();
                            iframedoc.writeln(scope.iframeResizer.getOptions().content + '<script src="vendor/iframe-resizer/js/iframeResizer.contentWindow.min.js"></script>');
                            iframedoc.close();

                            iFrameResize(scope.iframeResizer.getOptions(), element[0]);
                        }
                    }
                },
                restrict: 'A',
                scope: {
                    iframeResizer: '='
                }
            }
        }
    );
})();
