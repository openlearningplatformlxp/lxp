README
==========================
Instructions and notes for reduxl.

PREREQUISITES
===========================
* Bower must be installed - if needed:
    * `npm install -g bower`

SETUP WEBAPP
===========================
1. `npm install` (do not run as root: [fix permission issues](https://docs.npmjs.com/getting-started/fixing-npm-permissions))
1. `bower install`
1. `grunt serve`

MISC
===========================
* bash scripts
    * Install all node and bower components.
        * `./install.sh`
    * Remove all build artifacts (bower, dist, node, target, tmp, etc. directories).
        * `./cleanup.sh`

ADD BOWER LIBRARY
===========================
1. Add reference to bower.json using one of the following methods:
    * `bower --save install <package>`, or
    * Add reference to bower.json manually.
        * `bower install`
1. `grunt serve` -> this will add include to all needed files (e.g. index.html)

Page Titles
==========================
Page title are set in the module state provider config for each page.

Set $stateProvider config data.pageTitle to either a string value or a function that returns a string.

If not set, the page title will default to the PAGE_TITLE_DEFAULT value set in the app.

Docs from jHipster
==========================

# Developing

reduxl was generated using JHipster, you can find documentation and help at [JHipster][].

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the reduxl client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `reduxl`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/reduxl.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
