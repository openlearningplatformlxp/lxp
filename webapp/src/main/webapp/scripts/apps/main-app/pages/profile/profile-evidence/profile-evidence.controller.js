'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('ProfileEvidenceController',
        function($scope, Upload, ProfileEvidenceTypeService, $filter, dateInput, completedate) {


            var data = {
                    file: null,
                    showFileImage: false,
                    isSubmitting: false,
                    errorMessage: "",
                    showErrorMessage: false
                },

                getNumArray = function(num) {
                    return new Array(num);
                },
                addAnother = function() {
                    data.file = null;
                    data.errorMessage = "";
                    data.showErrorMessage = false;
                    data.showFileImage = false;
                    $scope.showSuccess = false;
                    data.isSubmitting = false;
                    $scope.evidence = {

                    }
                },
                isFormDisabled = function(form) {
                    return form.$invalid || form.$pristine;
                },
                uploadFile = function(file) {
                    data.showFileImage = false;
                    if (file != null) {
                        data.file = file;
                    }
                    data.showFileImage = true;
                },
                init = function() {
                    data.file = null;
                    data.errorMessage = "";
                    data.showErrorMessage = false;
                    $scope.showSuccess = false;
                    data.isSubmitting = false;
                    ProfileEvidenceTypeService.getEvidenceTypes(function(response) {
                        $scope.evidenceTypes = response;

                    }, function(error) {
                        console.log(error);
                    });

                    $scope.evidence = {

                    }
                },
                isDateValid = function(value) {
                    if (!value || value.length != 8) {
                        return false;
                    }

                    var valueMangled = value.substring(2, 4) + '/' + value.substring(0, 2) + '/' + value.substring(4, 8);

                    var re = /^(?=\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(?:\x20|$))|(?:2[0-8]|1\d|0?[1-9]))([-.\/])(?:1[012]|0?[1-9])\1(?:1[6-9]|[2-9]\d)?\d\d(?:(?=\x20\d)\x20|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\x20[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/;
                    var flag = re.test(valueMangled);

                    return flag;
                },
                sendEvidence = function() {
                    data.errorMessage = "";
                    data.showErrorMessage = false;

                    data.isSubmitting = true;

                    if (!isDateValid($scope.evidence.date)) {
                        data.errorMessage = "Date is not valid. " + $filter('completedate')($scope.evidence.date);
                        data.showErrorMessage = true;
                        data.isSubmitting = false;
                        return;
                    }

                    // or send them all together for HTML5 browsers:
                    Upload.upload({
                        url: 'api/evidence',
                        data: {
                            'file': data.file,
                            'creditId': $scope.evidence.creditId,
                            'name': $scope.evidence.name,
                            'description': $scope.evidence.activityType,
                            'externalActivityUrl': $scope.evidence.url,
                            'externalActivityInstitution': $scope.evidence.institution,
                            'date': $scope.evidence.date
                        }
                    }).then(function(resp) {
                        $scope.showSuccess = true;
                        data.isSubmitting = false;
                    }, function(resp) {
                    }, function(evt) {});
                },
                notYetImplemented = function() {
                    alert("Not yet Implemented");
                };

            init();

            return {
                data: data,
                addAnother: addAnother,
                getNumArray: getNumArray,
                isFormDisabled: isFormDisabled,
                uploadFile: uploadFile,
                sendEvidence: sendEvidence,
                notYetImplemented: notYetImplemented
            };
        }
    );

    module.factory('ProfileEvidenceTypeService', function($resource) {
        return $resource('api/evidence/types', {}, {
            getEvidenceTypes: {
                method: 'GET',
                isArray: true
            }
        });
    });





})();
