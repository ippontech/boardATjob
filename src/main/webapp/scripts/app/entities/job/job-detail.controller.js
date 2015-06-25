'use strict';

angular.module('boardatjobApp')
    .controller('JobDetailController', function ($scope, $stateParams, Job, Company) {
        $scope.job = {};
        $scope.load = function (id) {
            Job.get({id: id}, function(result) {
              $scope.job = result;
            });
        };
        $scope.load($stateParams.id);
    });
