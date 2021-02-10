'use strict';

(function() {
    var module = angular.module('app.components');

    module.factory('CardService', function(CourseService) {

        var view = 'list';

        var buildCard = function(item) {
                var tags = [],
                    type = null;
                return {
                    id: item.id,
                    sessionId: item.sessionId,
                    externalId: item.externalId,
                    date: item.eventTime,
                    city: item.city,
                    classType: item.classType,
                    country: item.country,
                    dueDate: item.dueDate,
                    timeZone: item.eventTimezone,
                    duration: CourseService.convertFloatDurationToHoursAndMinutes(item.duration),
                    status: 'ACTIVE',
                    type: item.type,
                    subtype: '',
                    title: item.title,
                    description: item.description,
                    sectionType: item.firstTopic,
                    cms: item.cms,
                    mobile: item.mobile,
                    externalUrl: item.externalUrl,
                    tags: tags,
                    courseStatus: item.courseStatus,
                    totaraUrl: appGlobal.config.get('totara.course.baseurl') + item.id,
                    personal: item.personal,
                    ceCredits: item.ceCredits,
                    manager: item.manager,
                    shared: item.shared,
                    sharedWithManager: item.sharedWithManager,
                    sharedWithManagerOn: item.sharedWithManagerOn,
                    shares: item.shares,
                    discoveryProgramType: item.discoveryProgramType,
                    discoveryProgramText: item.discoveryProgramText
                }
            },
            getView = function() {
                return view;
            },
            showGridView = function() {
                view = 'grid';
                return view;
            },
            showListView = function() {
                view = 'list';
                return view;
            },

            zend;

        return {
            getView: getView,
            showGridView: showGridView,
            showListView: showListView,
            buildCard: buildCard
        };
    });
})();
