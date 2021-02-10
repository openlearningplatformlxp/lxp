'use strict';

(function() {
    var module = angular.module('app.apps.main-app');

    module.controller('ProfilePreferencesController',
        function($scope, $rootScope, AlertsService, ProfilePreferencesService, ProfileInterestService, ProfileRoleService, $uibModal) {
            $scope.selectedInterest = null;
            var init = function() {
                    $scope.loading = true;
                    ProfilePreferencesService.getProfilePreferences(function(response) {
                        $scope.preferences = response;
                        $scope.loading = false;
                        $scope.preferences.roles.forEach(function(selectedRole) {
                            $scope.preferences.allRoles.forEach(function(role) {
                                if (role.id === selectedRole.id) {
                                    role.checked = true;
                                }
                            });
                        });
                    }, function(error) {
                    });
                },
                notYetImplemented = function() {
                    alert("Not yet Implemented");
                },
                addInterest = function(selectedInterest, index) {
                    if ($scope.selectedInterest != null) {
                        ProfileInterestService.addInterest($scope.selectedInterest.originalObject, function(response) {
                            $scope.preferences.interests = response;
                            $scope.$broadcast('angucomplete-alt:clearInput');
                        })

                    } else if (selectedInterest != null) {
                        ProfileInterestService.addInterest(selectedInterest, function(response) {
                            $scope.preferences.interests = response;
                            $scope.interests.splice(index, 1)
                        })
                    }
                },
                removeInterest = function(interest, index) {

                    ProfileInterestService.deleteInterest(interest, function(response) {
                        $scope.preferences.interests.splice(index, 1);
                    })
                },
                selectInterest = function(interest) {
                    $scope.selectedInterest = interest;
                },
                interestChanged = function(input) {
                    $scope.selectedInterest = null;
                },
                onCheckChange = function(role) {
                    if (role.checked) {
                        // object was checked. I should add it
                        addRole(role);
                    } else {
                        // object was unchecked. I should remove it
                        removeRole(role);
                    }
                },
                addRole = function(selectedRole) {
                    if (selectedRole != null) {
                        ProfileRoleService.addRole(selectedRole, function(response) {
                            $scope.preferences.roles = response;
                            $scope.$broadcast('angucomplete-alt:clearInput');
                        })

                    }
                },
                removeRole = function(role) {

                    ProfileRoleService.deleteRole(role, function(response) {
                    })
                },
                selectRole = function(role) {
                    $scope.selectedRole = role;
                },
                roleChanged = function(input) {
                    $scope.selectedRole = null;
                },

                updateInterestList = function() {
                    ProfileInterestService.getInterests(function(response) {
                        $scope.interests = response;
                    });
                },

                openInterestsModal = function() {
                    var modalInstance = $uibModal.open({
                        templateUrl: 'scripts/apps/main-app/pages/profile-preferences/support.html',
                        controller: function($scope, $uibModalInstance) {
                            $scope.ok = function() {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function() {
                                $uibModalInstance.dismiss('cancel');
                            };
                        },
                        backdrop: 'static'

                    });


                };

            init();

            return {
                init: init,
                addInterest: addInterest,
                removeInterest: removeInterest,
                interestChanged: interestChanged,
                selectInterest: selectInterest,
                onCheckChange: onCheckChange,
                roleChanged: roleChanged,
                selectRole: selectRole,
                notYetImplemented: notYetImplemented,
                openInterestsModal: openInterestsModal,
                updateInterestList: updateInterestList
            };
        }
    );

    module.factory('ProfilePreferencesService', function($resource) {
        return $resource('api/profile/preferences', {}, {
            getProfilePreferences: {
                method: 'GET'
            }
        });
    });

    module.factory('ProfileInterestService', function($resource) {
        return $resource('api/profile/interests/:id', {}, {
            getInterests: {
                method: 'GET',
                isArray: true
            },
            addInterest: {
                method: 'POST',
                isArray: true
            },
            deleteInterest: {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            }
        });
    });

    module.factory('ProfileRoleService', function($resource) {
        return $resource('api/profile/role/:id', {}, {
            addRole: {
                method: 'POST',
                isArray: true
            },
            deleteRole: {
                method: 'DELETE',
                params: {
                    id: "@id"
                }
            }
        });
    });

})();
