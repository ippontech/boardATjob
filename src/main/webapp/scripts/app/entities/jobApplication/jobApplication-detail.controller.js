'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationDetailController', function ($scope, $stateParams, JobApplication, User, Job) {
        $scope.jobApplication = {};
        $scope.load = function (id) {
            JobApplication.get({id: id}, function(result) {
              $scope.jobApplication = result;
            });
        };
        $scope.load($stateParams.id);
    });
