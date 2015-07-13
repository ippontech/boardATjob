'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationController', function ($scope, JobApplication, Job, UserProfile, JobApplicationSearch) {
        $scope.jobApplications = [];
        $scope.jobs = Job.query();
        $scope.userprofiles = UserProfile.query();
        $scope.loadAll = function() {
            JobApplication.query(function(result) {
               $scope.jobApplications = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            JobApplication.get({id: id}, function(result) {
                $scope.jobApplication = result;
                $('#saveJobApplicationModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.jobApplication.id != null) {
                JobApplication.update($scope.jobApplication,
                    function () {
                        $scope.refresh();
                    });
            } else {
                JobApplication.save($scope.jobApplication,
                    function () {
                        $scope.refresh();
                    });
            }
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
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
