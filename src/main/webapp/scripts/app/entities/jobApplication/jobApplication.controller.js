'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationController', function ($scope, JobApplication, User, Job, ParseLinks) {
        $scope.jobApplications = [];
        $scope.users = User.query();
        $scope.jobs = Job.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            JobApplication.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.jobApplications.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.jobApplications = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
                    $scope.reset();
                    $('#deleteJobApplicationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveJobApplicationModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jobApplication = {firstName: null, lastName: null, country: null, zipCode: null, phoneNumber: null, isAuthorized: null, id: null};
        };
    });
