'use strict';

angular.module('boardatjobApp')
    .controller('JobController', function ($scope, Job, Company, JobApplication, JobSearch) {
        $scope.jobs = [];
        $scope.companys = Company.query();
        $scope.jobapplications = JobApplication.query();
        $scope.loadAll = function() {
            Job.query(function(result) {
               $scope.jobs = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Job.get({id: id}, function(result) {
                $scope.job = result;
                $('#saveJobModal').modal('show');
            });
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
                    $scope.loadAll();
                    $('#deleteJobConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            JobSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jobs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveJobModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.job = {description: null, title: null, responsibilities: null, requirements: null, date: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
