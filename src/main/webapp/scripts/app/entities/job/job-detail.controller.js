'use strict';

angular.module('boardatjobApp')
    .controller('JobDetailController', function ($scope, $state, $stateParams, Principal, Job, Company, JobApplication) {
        $scope.job = {};
        $scope.load = function (id) {
            Job.get({id: id}, function(result) {
              $scope.job = result;
            });
        };
        $scope.load($stateParams.id);
        
        $scope.apply = function() {
        	if (Principal.isAuthenticated()) {
        		Principal.identity().then(function(identity) {
        			$state.go('apply', {jobId: $scope.job.id, userLogin: identity.login});
        		});
        		
        	} else {
        		$state.go('login');
        	}
        }
    });
