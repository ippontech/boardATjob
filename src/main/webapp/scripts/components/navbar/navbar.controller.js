'use strict';

angular.module('boardatjobApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.logout = function () {
            Auth.logout();
            $state.go('home', {}, {reload:true});
        };
    });
