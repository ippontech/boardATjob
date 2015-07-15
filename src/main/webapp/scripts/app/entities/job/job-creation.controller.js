angular.module('boardatjobApp')
    .controller('JobCreateController', function($scope, $state, Company, Job) {
        $scope.companys = Company.query();
        $scope.$state = $state;
        $scope.save = function () {
            if ($scope.job.id != null) {
                Job.update($scope.job,
                    function (res) {
                        $scope.clear();
                        $scope.$state.go('jobDetail', {id: job.id});
                    });
            } else {
                Job.save($scope.job,
                    function (res) {
                        $scope.clear();
                        $scope.$state.go('home');
                    });
            }
        };

        $scope.clear = function () {
            $scope.job = {title: null, description: null, responsibilities: null, requirements: null, postDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
