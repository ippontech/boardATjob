'use strict';

angular.module('boardatjobApp')
    .controller('UserProfileController', function ($scope, UserProfile, JobApplication, Company, UserProfileSearch) {
        $scope.userProfiles = [];
        $scope.jobapplications = JobApplication.query();
        $scope.companys = Company.query();
        $scope.loadAll = function() {
            UserProfile.query(function(result) {
               $scope.userProfiles = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            UserProfile.get({id: id}, function(result) {
                $scope.userProfile = result;
                $('#saveUserProfileModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.userProfile.id != null) {
                UserProfile.update($scope.userProfile,
                    function () {
                        $scope.refresh();
                    });
            } else {
                UserProfile.save($scope.userProfile,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            UserProfile.get({id: id}, function(result) {
                $scope.userProfile = result;
                $('#deleteUserProfileConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserProfile.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserProfileConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            UserProfileSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userProfiles = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserProfileModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userProfile = {email: null, name: null, phoneNumber: null, resume: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
