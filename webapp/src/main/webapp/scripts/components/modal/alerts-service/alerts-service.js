/**
 * General Alert Service.
 *
 * Common Options:
 *     modalClass: 'custom-modal-class', // default: undefined
 *     title: 'Title Text'
 *     text: 'Message Text'
 *     size: 'small', // 'small', 'medium', 'large', 'xlarge', 'xxlarge', 'NNNpx'
 *     showCloseIcon: true, // true, false - default: true (for non-confirm dialogs) - default: false (for cofirm dialogs)
 *
 * ***** Alert Options:
 *
 * button.text: 'Button Text'
 * button.class: 'Button Class' // defaults to btn-primary
 * button.onClick: Function called when button clicked. If returns false, alert does not close. Close function passed in first parameter.
 *
 * onX: function called when X icon is clicked -> defaults to button.onClick value
 *
 * ***** Confirm Options:
 *
 * buttonOk.text: 'OK Button Text'
 * buttonOk.class: 'OK Button Class' // defaults to btn-primary
 * buttonOk.onClick: Function called when button clicked. If returns false, alert does not close. Close function passed in first parameter.
 *
 * buttonCancel.text: 'Cancel Button Text'
 * buttonCancel.class: 'Cancel Button Class' // defaults to btn-cancel
 * buttonCancel.onClick: Function called when button clicked. If returns false, alert does not close. Close function passed in first parameter.
 *
 * onX: function called when X icon is clicked -> defaults to buttonCancel.onClick value
 *
 * ***** Modal Options:
 *
 * buttons: array of buttons
 *
 * buttons[{
 *     text: 'OK Button Text'
 *     class: 'OK Button Class' // defaults to btn-default
 *     position: 'left' // defaults to right
 *     onClick: Function called when button clicked. If returns false, alert does not close. Close function passed in first parameter.
 * }, ... ]
 *
 * onX: function called when X icon is clicked
 *
 * ***** Examples:
 *
 *     AlertsService.alert({
 *         title: 'Success',
 *         text: 'Course successfully completed! Yeah!',
 *     });
 *
 *     AlertsService.confirm({
 *         title: 'Confirm Delete',
 *         text: 'You are about to delete 13 users.',
 *         buttonOk.onClick: function(close) {
 *             ... do something ...
 *         }
 *     });
 *
 *     AlertsService.modal({
 *         title: 'Some Title',
 *         text: 'Some Text',
 *         buttons: [{
 *             class: 'btn-primary',
 *             onClick: function(close) {
 *                 ... do something ...
 *             },
 *             position: 'left',
 *             text: 'Button 1 Text'
 *         }, {
 *             onClick: function(close) {
 *                 ... do something ...
 *             },
 *             position: 'left',
 *             text: 'Button 2 Text'
 *         }, {
 *             onClick: function(close) {
 *                 ... do something ...
 *             },
 *             text: 'Button 1 Text'
 *         }]
 *     });
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.service('AlertsService',
        function($translate, vModal) {
            var zIndex = 2000,

                showModal = function(opts) {
                    var modal = undefined,

                        close = function(onClose, background) {
                            if (background && !opts.showCloseIcon) {
                                return;
                            }

                            if (angular.isFunction(onClose)) {
                                var close = onClose(closeAndRespond);

                                if (close || angular.isUndefined(close)) {
                                    closeAndRespond();
                                }
                            } else {
                                closeAndRespond();
                            }
                        },

                        closeAndRespond = function() {
                            modal.deactivate();

                            if (angular.isFunction(opts.onClose)) {
                                opts.onClose();
                            }
                        },

                        init = function() {
                            opts.buttonsLeft = [];
                            opts.buttonsRight = [];
                            opts.size = opts.size || 'small';
                            opts.text = opts.text || false;
                            opts.title = opts.title || false;
                            opts.onX = opts.onX || undefined;
                            opts.showCloseIcon = (angular.isDefined(opts.showCloseIcon) ? !!opts.showCloseIcon : true);

                            if (angular.isArray(opts.buttons)) {
                                angular.forEach(opts.buttons, function(button) {
                                    _addButton(button, opts);
                                });
                            }

                            modal = vModal({
                                controller: 'AlertsServiceController',
                                controllerAs: 'ctrl',
                                templateUrl: opts.templateUrl || 'scripts/components/modal/alerts-service/alerts-service.html'
                            });

                            show();
                        },

                        show = function() {
                            modal.activate({
                                opts: opts,
                                close: close,
                                zIndex: ++zIndex
                            });
                        };

                    init();

                    return {
                        close: closeAndRespond,
                        show: show
                    };
                };

            return {
                alert: function(options) {
                    var opts = angular.copy(options) || {};

                    opts.buttons = angular.isArray(opts.buttons) ? opts.buttons : [];

                    opts.buttons.push({
                        class: opts.button && opts.button.class ? opts.button.class : 'btn-primary',
                        id: opts.button && opts.button.id ? opts.button.id : 'ok',
                        onClick: opts.button && angular.isFunction(opts.button.onClick) ? opts.button.onClick : undefined,
                        text: opts.button && opts.button.text ? opts.button.text : 'Ok'
                    });

                    if (!angular.isFunction(opts.onX) && opts.button && angular.isFunction(opts.button.onClick)) {
                        opts.onX = opts.button.onClick;
                    }

                    return new showModal(opts);
                },

                confirm: function(options) {
                    var opts = angular.copy(options) || {};

                    opts.buttons = angular.isArray(opts.buttons) ? opts.buttons : [];

                    opts.buttons.push({
                        class: opts.buttonCancel && opts.buttonCancel.class ? opts.buttonCancel.class : 'btn-cancel',
                        id: opts.buttonCancel && opts.buttonCancel.id ? opts.buttonCancel.id : 'cancel',
                        onClick: opts.buttonCancel && angular.isFunction(opts.buttonCancel.onClick) ? opts.buttonCancel.onClick : undefined,
                        text: opts.buttonCancel && opts.buttonCancel.text ? opts.buttonCancel.text : 'Cancel'
                    });

                    opts.buttons.push({
                        class: opts.buttonOk && opts.buttonOk.class ? opts.buttonOk.class : 'btn-primary',
                        id: opts.buttonOk && opts.buttonOk.id ? opts.buttonOk.id : 'ok',
                        onClick: opts.buttonOk && angular.isFunction(opts.buttonOk.onClick) ? opts.buttonOk.onClick : undefined,
                        text: opts.buttonOk && opts.buttonOk.text ? opts.buttonOk.text : 'Ok'
                    });

                    if (!angular.isFunction(opts.onX) && opts.buttonCancel && angular.isFunction(opts.buttonCancel.onClick)) {
                        opts.onX = opts.buttonCancel.onClick;
                    }

                    opts.showCloseIcon = !!opts.showCloseIcon;

                    return new showModal(opts);
                },

                confirmCancel: function(options) {
                    var opts = angular.copy(options) || {};

                    opts.title = opts.title || $translate.instant('global.alerts.confirmCancel.title');
                    opts.text = opts.text || $translate.instant('global.alerts.confirmCancel.text');
                    opts.showCloseIcon = !!opts.showCloseIcon;

                    return this.confirm(opts);
                },

                httpError: function(response, options) {
                    var opts = angular.copy(options) || {},
                        text = (response.data.message ? '<p>' + response.data.message + '</p>' : '');

                    if (!opts.skipExtendedInfo && (response.statusText || response.status)) {
                        text += '<ul>';

                        if (response.statusText) {
                            text += '<li>Http Status Text: ' + response.statusText + '</li>';
                        }

                        if (response.status) {
                            text += '<li>Http Status: ' + response.status + '</li>';
                        }

                        text += '</ul>';
                    }

                    opts.text = text;

                    opts.buttons = angular.isArray(opts.buttons) ? opts.buttons : [];

                    opts.buttons.push({
                        class: opts.button && opts.button.class ? opts.button.class : 'btn-primary',
                        id: opts.button && opts.button.id ? opts.button.id : 'ok',
                        onClick: opts.button && angular.isFunction(opts.button.onClick) ? opts.button.onClick : undefined,
                        text: opts.button && opts.button.text ? opts.button.text : 'Ok'
                    });

                    if (!angular.isFunction(opts.onX) && opts.button && angular.isFunction(opts.button.onClick)) {
                        opts.onX = opts.button.onClick;
                    }

                    return new showModal(opts);
                },

                modal: function(options) {
                    var opts = angular.copy(options) || {};

                    return new showModal(opts);
                },

                notImplemented: function($event) {
                    if ($event) {
                        $event.preventDefault();
                        $event.stopPropagation();
                    }

                    this.alert({
                        title: 'Not Implemented!',
                        text: '<p>This feature is not implemented yet!</p>'
                    });
                }
            }
        }
    );

    module.controller('AlertsServiceController',
        function($sce, $scope) {
            var close = $scope.close,
                opts = $scope.opts,
                zIndex = $scope.zIndex,

                getButton = function(id) {
                    var i;

                    for (i = 0; i < opts.buttonsLeft.length; i++) {
                        if (opts.buttonsLeft[i].id == id) {
                            return opts.buttonsLeft[i];
                        }
                    }

                    for (i = 0; i < opts.buttonsRight.length; i++) {
                        if (opts.buttonsRight[i].id == id) {
                            return opts.buttonsRight[i];
                        }
                    }

                    return undefined;
                },

                getButtonsLeft = function() {
                    return opts.buttonsLeft;
                },

                getButtonsRight = function() {
                    return opts.buttonsRight;
                },

                getDialogStyle = function() {
                    var style = {};

                    if (!opts.size) {
                        opts.size = 'small';
                    }

                    if ('small' == opts.size) {
                        style['max-width'] = '400px';
                    } else if ('medium' == opts.size) {
                        style['max-width'] = '600px';
                    } else if ('large' == opts.size) {
                        style['max-width'] = '800px';
                    } else if ('xlarge' == opts.size) {
                        style['max-width'] = '1000px';
                    } else if ('xxlarge' == opts.size) {
                        style['max-width'] = '1100px';
                    } else {
                        style['max-width'] = opts.size;
                    }

                    return style;
                },

                getIncludeController = function() {
                    return opts.include.controller;
                },

                getIncludeResolve = function() {
                    if (!opts.include.resolve) {
                        opts.include.resolve = {};
                    }

                    opts.include.resolve.AlertsServiceDelegate = {
                        buttons: {
                            add: function(button) {
                                _addButton(button, opts);
                            },

                            enable: function(buttonId, enabled) {
                                var button = getButton(buttonId);

                                if (button) {
                                    button.disabled = !enabled;
                                }
                            },

                            setOnClick: function(buttonId, callback) {
                                var button = getButton(buttonId);

                                if (button) {
                                    var oldOnClick = button.onClick;

                                    button.onClick = function() {
                                        var success = callback();

                                        if (success || !angular.isDefined(success)) {
                                            if (angular.isFunction(oldOnClick)) {
                                                return oldOnClick();
                                            } else {
                                                return true;
                                            }
                                        }

                                        return false;
                                    }
                                }
                            }
                        },

                        close: close,

                        setText: function(text) {
                            opts.text = text || false;
                            opts.title = title || false;
                        },

                        setTitle: function(title) {
                            opts.title = title || false;
                        }
                    };

                    return opts.include.resolve;
                },

                getIncludeTemplateUrl = function() {
                    return opts.include.templateUrl;
                },

                getModalClass = function() {
                    return opts.modalClass || undefined;
                },

                getModalStyle = function() {
                    return {
                        'z-index': zIndex
                    };
                },

                getOnX = function() {
                    return opts.onX;
                },

                getText = function() {
                    return $sce.trustAsHtml(opts.text);
                },

                getTitle = function() {
                    return opts.title;
                },

                hasButtons = function() {
                    return hasButtonsLeft() || hasButtonsRight();
                },

                hasButtonsLeft = function() {
                    return opts.buttonsLeft.length > 0;
                },

                hasButtonsRight = function() {
                    return opts.buttonsRight.length > 0;
                },

                hasInclude = function() {
                    return (angular.isString(opts.include.templateUrl) && opts.include.templateUrl.length > 0);
                },

                hasText = function() {
                    return !!opts.text;
                },

                hasTitle = function() {
                    return !!opts.title;
                },

                init = function() {
                    if (!opts.include) {
                        opts.include = {};
                    }
                },

                showCloseIcon = function() {
                    return opts.showCloseIcon;
                },

                zend;

            init();

            return {
                getButtonsLeft: getButtonsLeft,
                getButtonsRight: getButtonsRight,
                getDialogStyle: getDialogStyle,
                getIncludeController: getIncludeController,
                getIncludeResolve: getIncludeResolve,
                getIncludeTemplateUrl: getIncludeTemplateUrl,
                getModalClass: getModalClass,
                getModalStyle: getModalStyle,
                getOnX: getOnX,
                getText: getText,
                getTitle: getTitle,
                hasButtons: hasButtons,
                hasButtonsLeft: hasButtonsLeft,
                hasButtonsRight: hasButtonsRight,
                hasInclude: hasInclude,
                hasText: hasText,
                hasTitle: hasTitle,
                showCloseIcon: showCloseIcon
            };
        }
    );

    // Private Helper Functions

    var _addButton = function(button, opts) {
        if (button.text) {
            var newButton = {
                class: button.class || 'btn-default',
                disabled: button.disabled || false,
                file: button.file || false,
                id: button.id || undefined,
                onClick: angular.isFunction(button.onClick) ? button.onClick : undefined,
                text: button.text
            };

            if (button.position === 'left') {
                opts.buttonsLeft.push(newButton);
            } else {
                opts.buttonsRight.push(newButton);
            }
        }
    };
})();
