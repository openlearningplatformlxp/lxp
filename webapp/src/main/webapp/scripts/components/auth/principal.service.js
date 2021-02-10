'use strict';

(function() {
    angular.module('app.components').factory('Principal',
        function Principal($q, $rootScope, Account) {
            var _identity,
                _authenticated = false,
                _authorities = {},

                authenticate = function(identity) {
                    _identity = identity;
                    _authenticated = identity !== null;
                },

                getAuthorities = function() {
                    return _authorities;
                },

                hasAnyAuthority = function(authorities) {
                    if (!_authenticated || !_identity || !_identity.authorities) {
                        return false;
                    }

                    for (var i = 0; i < authorities.length; i++) {
                        if (_identity.authorities.indexOf(authorities[i]) !== -1) {
                            return true;
                        }
                    }

                    return false;
                },

                hasAuthority = function(authority) {
                    if (!_authenticated) {
                        return $q.when(false);
                    }

                    return this.identity().then(function(_id) {
                        return _id.authorities && _id.authorities.indexOf(authority) !== -1;
                    }, function(err) {
                        return false;
                    });
                },

                identity = function(force) {
                    var deferred = $q.defer();

                    if (force !== true) {
                        // check and see if we have retrieved the identity data from the server.
                        // if we have, reuse it by immediately resolving
                        if (angular.isDefined(_identity)) {
                            deferred.resolve(_identity);

                            return deferred.promise;
                        }
                    }

                    // retrieve the identity data from the server, update the identity object, and then resolve.
                    Account.get().$promise.then(function(account) {
                        _identity = account.data;
                        _authenticated = true;

                        angular.forEach(_authorities, function(value, key) {
                            _authorities[key] = (_identity.authorities && _identity.authorities.indexOf(key) !== -1);
                        });

                        if (_identity.authorities && _identity.authorities.length > 0) {
                            angular.forEach(_identity.authorities, function(role) {
                                _authorities[role] = true;
                            });
                        }

                        deferred.resolve(_identity);
                    }).catch(function() {
                        _identity = null;
                        _authenticated = false;

                        angular.forEach(_authorities, function(value, key) {
                            _authorities[key] = false;
                        });

                        deferred.resolve(_identity);
                    }).finally(function() {
                        $rootScope.$broadcast('principal.identity.loaded', _identity);
                    });

                    return deferred.promise;
                },

                isAuthenticated = function() {
                    return _authenticated;
                },

                isIdentityResolved = function() {
                    return angular.isDefined(_identity);
                };

            return {
                authenticate: authenticate,
                getAuthorities: getAuthorities,
                hasAnyAuthority: hasAnyAuthority,
                hasAuthority: hasAuthority,
                identity: identity,
                isAuthenticated: isAuthenticated,
                isIdentityResolved: isIdentityResolved
            };
        }
    );
})();