angular.module('boardatjobApp')
    .controller('JobCreateController', function($scope, Job) {
        $scope.save = function () {
            if ($scope.job.id != null) {
                Job.update($scope.job,
                    function (res) {
                        $scope.clear();
                        $state.go('jobDetail', {id: job.id});
                    });
            } else {
                Job.save($scope.job,
                    function () {
                        $scope.clear();
                    });
            }
        };

        $scope.clear = function () {
            $scope.job = {title: null, description: null, responsibilities: null, requirements: null, postDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
