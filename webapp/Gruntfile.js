'use strict';
var fs = require('fs');
var serveStatic = require('serve-static');
var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;
var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', "utf8");
    parseString(pomXml, function(err, result) {
        version = result.project.version[0];
    });
    return version;
};

// usemin custom step
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: function(context, block) {
        if (block.src.length === 0) {
            return {};
        } else {
            return require('grunt-usemin/lib/config/cssmin').createConfig(context, block); // Reuse cssmins createConfig
        }
    }
};

var rewriteModule = require('http-rewrite-middleware');

module.exports = function(grunt) {
    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);

    var config = {
        livereload: {
            port: 35729,
            proxy: {
                context: '',
                host: 'localhost',
                port: 9000,
                remoteHost: 'localhost',
                remotePort: 8080
            },
            test: {
                port: 9100
            }
        }
    };

    grunt.initConfig({
        yeoman: {
            // configurable paths
            app: require('./bower.json').appPath || 'app',
            dist: 'src/main/webapp/dist'
        },
        watch: {
            bower: {
                files: ['bower.json'],
                tasks: ['wiredep']
            },
            ngconstant: {
                files: ['Gruntfile.js', 'pom.xml'],
                tasks: ['ngconstant:dev']
            },
            sass: {
                files: ['src/main/scss/**/*.{scss,sass}', 'src/main/webapp/scripts/**/*.{scss,sass}'],
                tasks: ['sass:server']
            },
            styles: {
                files: ['src/main/webapp/assets/styles/**/*.css']
            },
            livereload: {
                options: {
                    livereload: config.livereload.port
                },
                files: [
                    'src/main/webapp/**/*.html',
                    'src/main/webapp/**/*.json',
                    '{.tmp/,}src/main/webapp/assets/styles/**/*.css',
                    '{.tmp/,}src/main/webapp/scripts/**/*.js',
                    'src/main/webapp/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },
        autoprefixer: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        wiredep: {
            app: {
                src: ['src/main/webapp/admin-portal.html', 'src/main/webapp/index.html', 'src/main/scss/main.scss'],
                exclude: [
                    /angular-i18n/, // localizations are loaded dynamically
                    'bower_components/bootstrap/' // Exclude Bootstrap LESS as we use bootstrap-sass
                ],
                ignorePath: /\.\.\/webapp\/bower_components\// // remove ../webapp/bower_components/ from paths of injected sass files
            }
        },
        connect: {
            proxies: [{
                context: [
                    config.livereload.proxy.context + '/admin/',
                    config.livereload.proxy.context + '/api/',
                    config.livereload.proxy.context + '/api-docs/',
                    config.livereload.proxy.context + '/asset/',
                    config.livereload.proxy.context + '/asset-storage/',
                    config.livereload.proxy.context + '/configuration/',
                    config.livereload.proxy.context + '/metrics/',
                    config.livereload.proxy.context + '/pdf/',
                    config.livereload.proxy.context + '/swagger-ui.html',
                    config.livereload.proxy.context + '/swagger-resources/',
                    config.livereload.proxy.context + '/v2/',
                    config.livereload.proxy.context + '/webjars/'
                ],
                host: config.livereload.proxy.remoteHost,
                port: config.livereload.proxy.remotePort,
                https: false,
                changeOrigin: false
            }],
            options: {
                port: config.livereload.proxy.port,
                // Change this to 'localhost' to deny access to the server from outside.
                hostname: '0.0.0.0',
                livereload: config.livereload.port
            },
            livereload: {
                options: {
                    open: 'http://' + config.livereload.proxy.host + ':' + config.livereload.proxy.port + config.livereload.proxy.context,
                    base: [
                        '.tmp',
                        'src/main/webapp'
                    ],
                    middleware: function(connect) {
                        return [
                            proxySnippet,
                            rewriteModule.getMiddleware([{
                                    from: '^' + config.livereload.proxy.context + '$',
                                    to: '/'
                                },
                                {
                                    from: '^' + config.livereload.proxy.context + '/(.*)$',
                                    to: '/$1'
                                }
                            ]),
                            serveStatic('.tmp'),
                            serveStatic('src/main/webapp')
                        ];
                    }
                }
            },
            test: {
                options: {
                    port: config.livereload.test.port,
                    base: [
                        '.tmp',
                        'test',
                        'src/main/webapp'
                    ]
                }
            }
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= yeoman.dist %>/*',
                        '!<%= yeoman.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp'
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            all: [
                'Gruntfile.js',
                'src/main/webapp/scripts/**/*.js'
            ]
        },
        sass: {
            options: {
                includePaths: [
                    'src/main/webapp/bower_components'
                ]
            },
            server: {
                files: [{
                    expand: true,
                    cwd: 'src/main/scss',
                    src: ['*.scss'],
                    dest: 'src/main/webapp/assets/styles',
                    ext: '.css'
                }]
            }
        },
        concat: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        uglifyjs: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        rev: {
            dist: {
                files: {
                    src: [
                        '<%= yeoman.dist %>/scripts/**/*.js',
                        '<%= yeoman.dist %>/assets/styles/**/*.css',
                        '<%= yeoman.dist %>/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                        '<%= yeoman.dist %>/assets/fonts/*'
                    ]
                }
            }
        },
        useminPrepareMainApp: {
            html: 'src/main/webapp/**/*.html',
            options: {
                dest: '<%= yeoman.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat', 'uglifyjs'],
                            css: ['cssmin', useminAutoprefixer] // Let cssmin concat files so it corrects relative paths to fonts and images
                        },
                        post: {}
                    }
                }
            }
        },
        useminMainApp: {
            html: ['<%= yeoman.dist %>/**/*.html'],
            css: ['<%= yeoman.dist %>/assets/styles/**/*.css'],
            js: ['<%= yeoman.dist %>/scripts/apps/main-app/**/*.js', '<%= yeoman.dist %>/scripts/common/**/*.js', '<%= yeoman.dist %>/scripts/components/**/*.js', '<%= yeoman.dist %>/scripts/pages/**/*.js'],
            options: {
                assetsDirs: ['<%= yeoman.dist %>', '<%= yeoman.dist %>/assets/styles', '<%= yeoman.dist %>/assets/public', '<%= yeoman.dist %>/assets/images', '<%= yeoman.dist %>/assets/fonts'],
                patterns: {
                    js: [
                        [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
                    ]
                },
                dirs: ['<%= yeoman.dist %>']
            }
        },
        cssmin: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        ngtemplates: {
            dist: {
                cwd: 'src/main/webapp',
                src: ['scripts/**/*.html'],
                dest: '.tmp/templates/templates.js',
                options: {
                    module: 'app.common',
                    usemin: 'scripts/app.js',
                    htmlmin: '<%= htmlmin.dist.options %>'
                }
            }
        },
        htmlmin: {
            dist: {
                options: {
                    removeCommentsFromCDATA: true,
                    // https://github.com/yeoman/grunt-usemin/issues/44
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true,
                    conservativeCollapse: true,
                    removeAttributeQuotes: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true,
                    keepClosingSlash: true
                },
                files: [{
                    expand: true,
                    cwd: '<%= yeoman.dist %>',
                    src: ['*.html'],
                    dest: '<%= yeoman.dist %>'
                }]
            }
        },
        // Put files not handled in other tasks here
        copy: {
            fonts: {
                files: [{
                    expand: true,
                    dot: true,
                    flatten: true,
                    cwd: 'src/main/webapp',
                    dest: 'src/main/webapp/assets/fonts',
                    src: [
                        'bower_components/bootstrap-sass/assets/fonts/bootstrap/*.*',
                        'bower_components/font-awesome/fonts/*.*',
                        'bower_components/feather-icons-sass/fonts/*.*',
                        'bower_components/overpass/webfonts/overpass-webfont/*.*'
                    ]
                }]
            },
            vendor: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: 'src/main/webapp/bower_components',
                    dest: 'src/main/webapp/vendor',
                    src: [
                        'iframe-resizer/**'
                    ]
                }]
            },
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: 'src/main/webapp',
                    dest: '<%= yeoman.dist %>',
                    src: [
                        '*.html',
                        'scripts/**/*.html',
                        'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',
                        'assets/public/**/*.{png,gif,webp,jpg,jpeg,svg}',
                        'assets/fonts/*',
                        'vendor/**'
                    ]
                }, {
                    expand: true,
                    cwd: '.tmp/assets/images',
                    dest: '<%= yeoman.dist %>/assets/images',
                    src: [
                        'generated/*'
                    ]
                }]
            }
        },
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/scripts',
                    src: '*.js',
                    dest: '.tmp/concat/scripts'
                }]
            }
        },
        ngconstant: {
            options: {
                name: 'reduxl.common',
                wrap: '"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
            },
            dev: {
                options: {
                    dest: 'src/main/webapp/scripts/common/app.constants.js'
                },
                constants: {
                    WALKME: {
                        SCRIPT: 'https://cdn.walkme.com/users/df9f1b2a19294c828f98d451211a6ecf/test/walkme_df9f1b2a19294c828f98d451211a6ecf_https.js'
                    },
                    BUILD: {
                        INFO: {
                            BUILD_DATE_TIME: grunt.template.today("yyyy-mm-dd h:MM:ss TT"),
                            BUILD_ENV: 'dev',
                            BUILD_VERSION: parseVersionFromPomXml()
                        }
                    }
                }
            },
            test: {
                options: {
                    dest: 'src/main/webapp/scripts/common/app.constants.js'
                },
                constants: {
                    WALKME: {
                        SCRIPT: 'https://cdn.walkme.com/users/df9f1b2a19294c828f98d451211a6ecf/test/walkme_df9f1b2a19294c828f98d451211a6ecf_https.js'
                    },
                    BUILD: {
                        INFO: {
                            BUILD_DATE_TIME: grunt.template.today("yyyy-mm-dd h:MM:ss TT"),
                            BUILD_ENV: 'prod',
                            BUILD_VERSION: parseVersionFromPomXml()
                        }
                    }
                }
            },
            prod: {
                options: {
                    dest: 'src/main/webapp/scripts/common/app.constants.js'
                },
                constants: {
                    WALKME: {
                        SCRIPT: 'https://cdn.walkme.com/users/df9f1b2a19294c828f98d451211a6ecf/walkme_df9f1b2a19294c828f98d451211a6ecf_https.js'
                    },
                    BUILD: {
                        INFO: {
                            BUILD_DATE_TIME: grunt.template.today("yyyy-mm-dd h:MM:ss TT"),
                            BUILD_ENV: 'prod',
                            BUILD_VERSION: parseVersionFromPomXml()
                        }
                    }
                }
            }
        }
    });

    grunt.registerTask('useminPrepareMainApp', function() {
        var useminPrepareMainAppConfig = grunt.config('useminPrepareMainApp');
        grunt.config.set('useminPrepare', useminPrepareMainAppConfig);
        grunt.task.run('useminPrepare');
    });

    grunt.registerTask('useminMainApp', function() {
        var useminMainAppConfig = grunt.config('useminMainApp');
        grunt.config.set('usemin', useminMainAppConfig);
        grunt.task.run('usemin');
    });

    grunt.registerTask('serve', [
        'clean:server',
        'wiredep',
        'ngconstant:dev',
        'sass:server',
        'copy:fonts',
        'copy:vendor',
        'configureProxies',
        'connect:livereload',
        'watch'
    ]);

    grunt.registerTask('server', function(target) {
        grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
        grunt.task.run([target ? ('serve:' + target) : 'serve']);
    });

    grunt.registerTask('test', [
        // TODO: SAC: implement tests
    ]);

    grunt.registerTask('buildUAT', [
        'clean:dist',
        'wiredep:app',
        'ngconstant:test',
        'useminPrepareMainApp',
        'ngtemplates',
        'sass:server',
        'concat',
        'copy:fonts',
        'copy:vendor',
        'copy:dist',
        'ngAnnotate',
        'cssmin',
        'autoprefixer',
        // TODO: SAC: need to find out why jHipster has this: 'replace',
        'uglify',
        'rev',
        'useminMainApp',
        'htmlmin'
    ]);

    grunt.registerTask('build', [
        'clean:dist',
        'wiredep:app',
        'ngconstant:prod',
        'useminPrepareMainApp',
        'ngtemplates',
        'sass:server',
        'concat',
        'copy:fonts',
        'copy:vendor',
        'copy:dist',
        'ngAnnotate',
        'cssmin',
        'autoprefixer',
        // TODO: SAC: need to find out why jHipster has this: 'replace',
        'uglify',
        'rev',
        'useminMainApp',
        'htmlmin'
    ]);

    grunt.registerTask('default', ['serve']);
};