'use strict';

angular.module('boardatjobApp')
    .controller('JobPostController', function ($scope, JobPost, ParseLinks) {
        $scope.jobPosts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            JobPost.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.jobPosts.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.jobPosts = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            JobPost.get({id: id}, function(result) {
                $scope.jobPost = result;
                $('#saveJobPostModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.jobPost.id != null) {
                JobPost.update($scope.jobPost,
                    function () {
                        $scope.refresh();
                    });
            } else {
                JobPost.save($scope.jobPost,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            JobPost.get({id: id}, function(result) {
                $scope.jobPost = result;
                $('#deleteJobPostConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            JobPost.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteJobPostConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveJobPostModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jobPost = {title: null, description: null, requirements: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
