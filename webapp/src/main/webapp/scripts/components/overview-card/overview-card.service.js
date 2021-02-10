'use strict';

(function() {
    var module = angular.module('app.components');

    module.factory('OverviewCardService', function() {

        var buildCard = function(item, percentComplete) {

            var tags = [];
            if (item.personal) {
                tags = ['personal'];
            }
            var dueDate = null;
            if (item.dueDate) {
                dueDate = moment(item.dueDate).format('MM/DD/YYYY');
            }

            return {
                id: item.id,
                date: item.dueDate,
                time: item.dueDate,
                status: 'ACTIVE',
                itemType: item.type,
                title: item.title,
                personal: item.personal,
                description: item.description,
                dueDate: dueDate,
                sectionType: item.firstTopic,
                mobile: item.mobile,
                tags: tags,
                manager: item.manager,
                shared: item.shared,
                shares: item.shares,
                sharedWithManager: item.sharedWithManager,
                sharedWithManagerOn: item.sharedWithManagerOn,
                sharedWithReports: item.sharedWithReports,
                ownerId: item.ownerId,
                percentComplete: percentComplete || 0
            }
        };

        return {
            buildCard: buildCard
        };
    });
})();
