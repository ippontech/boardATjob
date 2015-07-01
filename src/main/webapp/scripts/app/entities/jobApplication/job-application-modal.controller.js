'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationModalController', function($scope, jobId, currentUser, User) {
        User.get({login: currentUser.login}, function(success) {
            $scope.currentUserId = success.id;
        });

        
        $scope.jobApplication.createdBy = {};
        $scope.jobApplication.createdFor = {};
        $scope.jobApplication.createdBy.id = $scope.currentUserId;
        $scope.jobApplication.createdFor.id = jobId;

        $scope.save = function () {
            JobApplication.save($scope.jobApplication,
                function () {
                    $scope.refresh();
                });
            }
    });
