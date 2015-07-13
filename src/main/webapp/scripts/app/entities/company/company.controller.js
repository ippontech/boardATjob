'use strict';

angular.module('boardatjobApp')
    .controller('CompanyController', function ($scope, Company, Job, UserProfile, CompanySearch) {
        $scope.companys = [];
        $scope.jobs = Job.query();
        $scope.userprofiles = UserProfile.query();
        $scope.loadAll = function() {
            Company.query(function(result) {
               $scope.companys = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Company.get({id: id}, function(result) {
                $scope.company = result;
                $('#saveCompanyModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.company.id != null) {
                Company.update($scope.company,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Company.save($scope.company,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Company.get({id: id}, function(result) {
                $scope.company = result;
                $('#deleteCompanyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Company.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCompanyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CompanySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.companys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCompanyModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.company = {name: null, description: null, location: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
