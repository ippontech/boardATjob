angular.module('boardatjobApp')
    .controller('HeaderController', function($scope, Principal) {
       $scope.isAuthenticated = Principal.isAuthenticated;
    });
