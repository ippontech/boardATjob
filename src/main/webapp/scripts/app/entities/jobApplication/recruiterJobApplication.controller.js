'use strict';

angular.module('boardatjobApp')
    .controller('RecruiterJobApplicationController', function ($scope, JobApplication, Job, UserProfile, JobApplicationSearch, Principal) {
        $scope.jobApplications = [];
        //$scope.jobs = Job.query();
        $scope.companyId = null;
        $scope.loadAll = function() {
            JobApplication.getByCompany({companyId: $scope.companyId}, function(result) {
            	console.log(result);
               $scope.jobApplications = result;
            });
        };
        Principal.identity().then(function(account) {
        	UserProfile.getByLogin({login: account.login}, function(result) {
        		$scope.companyId = result.company ? result.company.id : null;
        		console.log('company id: ' + $scope.companyId);
        		$scope.loadAll();
        	});
        });
        
        $scope.showDetail = function(id) {
        	JobApplication.get({id: id}, function(result) {
                $scope.jobApplication = result;
            	$('#viewJobApplication').modal('show');
            });
        };
        
        $scope.delete = function (id) {
            JobApplication.get({id: id}, function(result) {
                $scope.jobApplication = result;
                $('#deleteJobApplicationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            JobApplication.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteJobApplicationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            JobApplicationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jobApplications = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveJobApplicationModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jobApplication = {coverLetter: null, id: null};
            //$scope.editForm.$setPristine();
            //$scope.editForm.$setUntouched();
        };
        
    });
