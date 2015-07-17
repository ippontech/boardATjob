'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationApplyController', function ($rootScope, $scope, $stateParams, JobApplication, Job, UserProfile) {
        $scope.jobApplication = {};
        $scope.applied = false;
        $scope.load = function (jobId, userLogin) {
        	console.log($stateParams);
        	console.log('jobid: ', jobId, ' userLogin: ', userLogin)
        	if (jobId && userLogin) {
            	JobApplication.getByJobAndLogin({jobid: jobId, login: userLogin}, function(result) {
            		console.log("JobApplication: ", result);
	                $scope.jobApplication = result;
	                $scope.applied = true;
	            });
        	}
        	
        	if (jobId != null) {
        		Job.get({id: jobId}, function(result) {
        			$scope.jobApplication.job = result;
        		});
        	}
            
        	if (userLogin != null) {
        		UserProfile.getByLogin({login: userLogin}, function(result) {
        			$scope.jobApplication.userProfile = result;
        		});
        	}
        };
        $scope.load($stateParams.jobId, $stateParams.userLogin);
        
        $scope.submitApplication = function() {
        	JobApplication.save($scope.jobApplication,
                    function () {
                        $scope.applied = true;
                    });
        };
    });
