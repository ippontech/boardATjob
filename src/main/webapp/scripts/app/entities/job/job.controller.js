'use strict';

angular.module('boardatjobApp')
    .controller('JobController', function ($scope, Job, Company, ParseLinks, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.jobs = [];
        $scope.companys = Company.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Job.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.jobs.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.jobs = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
                    $scope.reset();
                    $('#deleteJobConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveJobModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.job = {title: null, description: null, responsibilities: null, requirements: null, postDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
