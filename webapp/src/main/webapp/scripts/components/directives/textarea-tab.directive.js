/**
 * From: https://github.com/OakBehringer/ak-tabby-textarea/blob/master/src/ak-tabby-textarea.js
 *
 * Also to do Multi-Line: http://codepen.io/zulns/pen/yeEejM
 */

'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('textareaTab',
        function() {

            var insertAt = function(textArea, value) {
                var node = angular.element(textArea);
                node.val(node.val().substring(0, textArea.selectionStart) + value + node.val().substring(textArea.selectionEnd));
            };

            return {
                restrict: 'A',
                link: function(scope, elem, attrs) {
                    elem.bind('keydown', function(e) {
                        // TAB Key
                        if (e.keyCode === 9) {
                            e.preventDefault();
                            var start = this.selectionStart,
                                numSpaces = 4 - (start % 4),
                                spaces = numSpaces == 4 ? '    ' : numSpaces == 3 ? '   ' : numSpaces == 2 ? '  ' : ' ';

                            insertAt(this, spaces);
                            this.selectionStart = this.selectionEnd = start + numSpaces;
                            return;
                        }

                        // ENTER key
                        if (e.keyCode === 13) {
                            var curLine = this.value.substr(0, this.selectionStart).split("\n").pop();
                            var numStartSpaces = curLine.match(/^ */).shift().length;
                            var that = this;

                            if (numStartSpaces) {
                                e.preventDefault();
                                var insert = "\n";
                                var start = that.selectionStart;

                                while (numStartSpaces > 0) {
                                    insert += " ";
                                    numStartSpaces--;
                                }

                                insertAt(that, insert);
                                that.selectionStart = that.selectionEnd = start + insert.length;
                            }
                        }
                    })
                }
            }
        }
    );

    /**
     * The MIT License (MIT)
     *
     * Copyright (c) 2013 Thom Seddon
     * Copyright (c) 2010 Google
     *
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     *
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     *
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
     * THE SOFTWARE.
     * Adapted from: http://code.google.com/p/gaequery/source/browse/trunk/src/static/scripts/jquery.autogrow-textarea.js
     *
     * Works nicely with the following styles:
     * textarea {
     *  resize: none;
     *  word-wrap: break-word;
     *  transition: 0.05s;
     *  -moz-transition: 0.05s;
     *  -webkit-transition: 0.05s;
     *  -o-transition: 0.05s;
     * }
     *
     * Usage: <textarea auto-grow></textarea>
     */

    module.directive('autoGrow', function($timeout) {
        return function(scope, element, attr) {
            var minHeight = element[0].offsetHeight,
                paddingLeft = element.css('paddingLeft'),
                paddingRight = element.css('paddingRight');

            var $shadow = angular.element('<div></div>').css({
                position: 'absolute',
                top: -10000,
                left: -10000,
                width: element[0].offsetWidth - parseInt(paddingLeft || 0) - parseInt(paddingRight || 0),
                fontSize: element.css('fontSize'),
                fontFamily: element.css('fontFamily'),
                lineHeight: element.css('lineHeight'),
                resize: 'none'
            });

            angular.element(document.body).append($shadow);

            var update = function() {
                var times = function(string, number) {
                    for (var i = 0, r = ''; i < number; i++) {
                        r += string;
                    }
                    return r;
                };

                var val = element.val().replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/&/g, '&amp;')
                    .replace(/\n$/, '<br/>&nbsp;')
                    .replace(/\n/g, '<br/>')
                    .replace(/\s{2,}/g, function(space) {
                        return times('&nbsp;', space.length - 1) + ' '
                    });
                $shadow.html(val);

                element.css('height', Math.max($shadow[0].offsetHeight + 20 /* the "threshold" */ , minHeight) + 'px');
            };

            element.bind('keyup keydown keypress change', update);

            $timeout(function() {
                update();
            }, 1);
        }
    });
})();
