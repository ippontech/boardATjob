'use strict';

angular.module('boardatjobApp')
    .controller('UserProfileDetailController', function ($scope, $stateParams, UserProfile, JobApplication, Company) {
        $scope.userProfile = {};
        $scope.load = function (id) {
            UserProfile.get({id: id}, function(result) {
              $scope.userProfile = result;
            });
        };
        $scope.load($stateParams.id);
    });
