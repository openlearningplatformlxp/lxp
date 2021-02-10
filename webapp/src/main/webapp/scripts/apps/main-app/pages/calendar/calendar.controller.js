'use strict';

/*!
 * FullCalendar v3.9.0
 * Docs & License: https://fullcalendar.io/
 * (c) 2018 Adam Shaw
 */

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('CalendarController',
        function($state, $http, CalendarTagsService) {
        var data = {};
            data.filterTags = {
                skillLevels: null,
                languages: null
            };
            data.filters = {};

            var buildCalendar = function() {
                var date = new Date();
                var d = date.getDate();
                var m = date.getMonth();
                var y = date.getFullYear();

                /* initialize the calendar
                -----------------------------------------------------------------*/

                var calendar = $('.calendar').fullCalendar({
                    header: {
                        left: 'title',
                        center: '',
                        right: 'month listWeekUx prev,next'
                    },
                    aspectRatio: 1.5,
                    editable: false,
                    firstDay: 0, //  1(Monday) this can be changed to 0(Sunday) for the USA system
                    selectable: true,
                    defaultView: 'month',
                    axisFormat: 'h:mm',
                    columnFormat: 'dddd',
                    titleFormat: 'LL',
                    allDaySlot: false,
                    selectHelper: true,
                    eventSources: [{
                        url: "api/calendar/",
                        eventDataTransform: function(event) {
                            var eventType = event['type'].name;
                            event.textColor = 'rgb(255,255,255)';
                            if (eventType.toLowerCase() === 'professional') {
                                event.backgroundColor = 'rgb(143, 0, 0)';
                                event.borderColor = 'rgb(143, 0, 0)';
                            } else if (eventType.toLowerCase() === 'manager') {
                                event.backgroundColor = 'rgb(3, 122, 157)';
                                event.borderColor = 'rgb(3, 122, 157)';
                            } else if (eventType.toLowerCase() === 'sales') {
                                event.backgroundColor = 'rgb(78, 116, 92)';
                                event.borderColor = 'rgb(78, 116, 92)';
                            } else if (eventType.toLowerCase() === 'technical') {
                                event.backgroundColor = 'rgb(40, 104, 105)';
                                event.borderColor = 'rgb(40, 104, 105)';
                            } else {
                                event.backgroundColor = 'rgb(108, 108, 108)';
                                event.borderColor = 'rgb(108, 108, 108)';
                            }
                            return event;
                        }
                    }],
                    eventLimit: 5,
                    views: {
                        listWeekUx: {
                            type: 'listWeek',
                            dayCount: 7
                        }
                    },
                    eventRender: function(event, element, view) {
                        $(element).find('.fc-list-item-title').append("<span class='fc-list-item-location'><span class='fc-list-item-city'>" + event.city + "</span>, <span class='fc-list-item-country'>" + event.country + "</span></span>")
                    },
                    dayClick: function(date, jsEvent, view) {
                        if (view.name != 'listWeekUx') {
                            $('.calendar').fullCalendar('changeView', 'listWeekUx', date);
                        }
                    },
                    eventClick: function(calEvent, jsEvent, view) {
                        $state.go("course-totara", {
                            courseId: calEvent.id,
                            source: 'calendar'
                        });
                    }
                });

            };

            var rerenderCalendar = function() {
                var country = data.filters['country'] != undefined && data.filters['country'] != null ? data.filters['country'] : '';
                var city = data.filters['city'] != undefined && data.filters['city'] != null ? data.filters['city'] : '';
                $('.calendar').fullCalendar('removeEventSources');
                $('.calendar').fullCalendar('addEventSource', "api/calendar/?country=" + country + '&city=' + city);
                $('.calendar').fullCalendar('refetchEvents');
            };

            var findTags = function() {
                CalendarTagsService.findTags({}, function(response) {
                    data.filterTags = response;
                }, function(error) {
                    console.log(error);
                });
            };

            var init = function() {
                findTags();
                $(document).ready(function() {
                    buildCalendar();
                });
            };

            init();

            return {
                data: data,
                rerenderCalendar: rerenderCalendar,
            };
        }
    );

    module.factory('CalendarTagsService', function($resource) {
        return $resource('api/search/tags', {}, {
            findTags: {
                method: 'GET'
            }
        });
    });

})();
