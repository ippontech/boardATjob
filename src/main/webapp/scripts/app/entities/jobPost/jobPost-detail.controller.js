'use strict';

angular.module('boardatjobApp')
    .controller('JobPostDetailController', function ($scope, $stateParams, JobPost) {
        $scope.jobPost = {};
        $scope.load = function (id) {
            JobPost.get({id: id}, function(result) {
              $scope.jobPost = result;
            });
        };
        $scope.load($stateParams.id);
    });
