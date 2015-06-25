'use strict';

angular.module('boardatjobApp')
    .controller('CompanyDetailController', function ($scope, $stateParams, Company, Job) {
        $scope.company = {};
        $scope.load = function (id) {
            Company.get({id: id}, function(result) {
              $scope.company = result;
            });
        };
        $scope.load($stateParams.id);
    });
