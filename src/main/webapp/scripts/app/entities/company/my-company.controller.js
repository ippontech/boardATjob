'use strict';

angular.module('boardatjobApp')
    .controller('MyCompanyController', function ($scope, $stateParams, Company, Job, UserProfile, Principal) {
    	$scope.profile = {};
        $scope.load = function (login) {
            UserProfile.getByLogin({login: login}, function(result) {
              $scope.profile = result;
              console.log("User Profile: ",$scope.profile);
              if (!$scope.profile.company) {
            	  $scope.profile.company = {};
              } else {
            	  $scope.loadCompany();
              }
            });
        };
        Principal.identity().then(function(account) {
        	$scope.load(account.login);
        });
        
        $scope.loadCompany = function() {
        	Company.get({id: $scope.profile.company.id}, function(result) {
        		console.log('loading company', result);
          	  	$scope.profile.company = result;
        	});
        };
        
        $scope.save = function () {
            UserProfile.update($scope.profile,
            function (result) {
        		console.log(result);
        		$scope.profile = result;
        		
            });
        }
        
    });
