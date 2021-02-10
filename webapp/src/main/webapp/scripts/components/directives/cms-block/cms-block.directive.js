'use strict';

(function() {
    var module = angular.module('app.components');

    module.directive('cmsBlock',
        function($compile, $http, $rootScope, $translate, Principal, TextEditorModal) {
            var makeEditContent = function(content) {
                    var after = '',
                        before = '';

                    before += '<div ng-class="cmsBlockEditBlockClass">';
                    before += '    <div class="cms-block-edit-block-inner">';
                    before += '        <div ng-if="cmsBlockEditBlockClass" class="cms-block-edit-block-actions" ng-click="editCmsBlock()"><i class="fa fa-pencil" aria-hidden="true"></i></div>';

                    after += '    </div>';
                    after += '</div>';

                    if (!content) {
                        content = '&nbsp;';
                    }

                    return before + content + after;
                },

                saveCmsBlock = function(cmsBlock) {
                    var data = {
                        id: cmsBlock.id ? cmsBlock.id : undefined,
                        key: cmsBlock.key,
                        name: cmsBlock.id ? cmsBlock.name : cmsBlock.key,
                        description: cmsBlock.id ? cmsBlock.description : cmsBlock.key,
                        content: cmsBlock.content
                    };

                    return $http.put('api/cms/block' + (cmsBlock.id ? '/' + cmsBlock.id : ''), data);
                };

            return {
                compile: function(element) {
                    var originalContent = undefined,
                        originalConentSaved = false;

                    if (!originalConentSaved) {
                        originalConentSaved = true;
                        originalContent = element.html();
                    }

                    return {
                        pre: function(scope, element) {
                            var blocks = appGlobal.config.get('blocks'),
                                block = blocks[scope.key];

                            if (!block) {
                                block = {
                                    key: scope.key
                                };

                                blocks[scope.key] = block;
                            }

                            if (!block.content) {
                                block.content = originalContent;
                            }
                        },
                        post: function(scope, element) {
                            var blocks = appGlobal.config.get('blocks'),
                                block = blocks[scope.key];

                            var content = block.content,
                                self = this;

                            Principal.hasAuthority('ROLE_CMS_EDITOR').then(function(result) {
                                if (result) {
                                    // EDITOR

                                    content = makeEditContent(content);

                                    scope.cmsBlockEditBlockClass = 'cms-block-directive';

                                    scope.editCmsBlock = function() {
                                        var editContent = {
                                            value: block.content
                                        };

                                        TextEditorModal.showTextEditor({
                                            content: editContent,
                                            buttonOk: {
                                                onClick: function() {
                                                    block.content = editContent.value;

                                                    saveCmsBlock(block).then(function(response) {
                                                        if (!block.id) {
                                                            // block was newly created
                                                            block = response.data;

                                                            blocks[scope.key] = block;
                                                        }

                                                        $rootScope.$broadcast('cms-block.directive.saved', block);
                                                    }, function(response) {
                                                        alert('error')
                                                    });
                                                }
                                            },
                                            title: '&nbsp; <i class="fa fa-th" aria-hidden="true"></i> ' + $translate.instant('cms-block-add-edit.title.edit')
                                        });
                                    };

                                    element.html(content);

                                    $compile(element.contents())(scope);
                                } else {
                                    // NOT EDITOR

                                    if (content) {
                                        element.html(content);

                                        $compile(element.contents())(scope);
                                    }
                                }
                            });

                            $rootScope.$on('cms-block.directive.saved', function(event, newCmsBlock) {
                                if (newCmsBlock.key === block.key) {
                                    block = newCmsBlock;

                                    element.html(makeEditContent(block.content));

                                    $compile(element.contents())(scope);
                                }
                            });
                        }
                    }
                },
                restrict: 'E',
                scope: {
                    key: '@',
                    ctrl: '='
                }
            }
        }
    );
})();
