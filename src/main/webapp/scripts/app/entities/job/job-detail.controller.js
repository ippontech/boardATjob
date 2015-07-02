'use strict';

angular.module('boardatjobApp')
    .controller('JobDetailController', function ($scope, $stateParams, $state, Job, Company) {
        $scope.job = {};
        $scope.load = function (id) {
            Job.get({id: id}, function(result) {
              $scope.job = result;
            });
        };
        $scope.load($stateParams.id);
        $scope.isPublic = $state.current.data.public;
        console.log($scope.isPublic);
    });
