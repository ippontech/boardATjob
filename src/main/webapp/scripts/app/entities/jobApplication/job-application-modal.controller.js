'use strict';

angular.module('boardatjobApp')
    .controller('JobApplicationModalController', function($scope, $modalInstance, $state, jobId, currentUser, User, JobApplication) {
        $scope.$state = $state;


        $scope.jobApplication = {};
        $scope.jobApplication.createdBy = {};
        $scope.jobApplication.createdFor = {};
        $scope.jobApplication.createdFor.id = jobId;

        User.get({login: currentUser.login}, function(success) {
            $scope.jobApplication.createdBy.id = success.id;
        });

        $scope.save = function () {
            JobApplication.save($scope.jobApplication,
                function () {
                    $scope.refresh();
                });
            };

        $scope.refresh = function() {
            $modalInstance.close();
            $scope.clear();
            $modalInstance.result.then(function() {
                $scope.$state.go('^');
            });
        };

        $scope.closeModal = function() {
            $modalInstance.dismiss();
            $scope.clear();
            $scope.$state.go('^');
        };

        // Handles closing the modal via escape and clicking outside the modal
        $modalInstance.result.finally(function() {
            $scope.$state.go('^');
        });

        $scope.clear = function () {
            $scope.jobApplication = {firstName: null, lastName: null, country: null, zipCode: null, phoneNumber: null, isAuthorized: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
