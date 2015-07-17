'use strict';

angular.module('boardatjobApp')
    .controller('RecruiterJobController', function ($rootScope, $scope, $state, Job, Company, ParseLinks, Principal, JobApplication, UserProfile) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.jobs = [];
        $scope.loadAll = function() {
            Job.getByCompany({companyId: $scope.companyId}, function(result, headers) {
            	$scope.jobs = result;
            });
        };
        Principal.identity().then(function(account) {
        	UserProfile.getByLogin({login: account.login}, function(result) {
        		$scope.companyId = result.company.id;
        		console.log('company id: ' + $scope.companyId);
        		$scope.loadAll();
        	});
        });
        
        $scope.editJob = function (id) {
            $state.go('jobCreate', {'id': id});
        };

        $scope.save = function () {
            if ($scope.job.id != null) {
                Job.update($scope.job,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Job.save($scope.job,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Job.get({id: id}, function(result) {
                $scope.job = result;
                $('#deleteJobConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Job.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteJobConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            JobSearch.query({query: $scope.searchQuery}, function(result) {
            	console.log(result);
                $scope.jobs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.isSearchText = function() {
        	return $scope.searchQuery != null && $scope.searchQuery.length > 0;
        };
        
        $scope.refresh = function () {
            $scope.reset();
            $('#saveJobModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.job = {description: null, title: null, responsibilities: null, requirements: null, date: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
