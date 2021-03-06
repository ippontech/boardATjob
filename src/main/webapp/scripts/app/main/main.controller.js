'use strict';

angular.module('boardatjobApp')
    .controller('MainController', function ($rootScope, $scope, Principal) {
    	
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
});
