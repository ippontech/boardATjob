angular.module('boardatjobApp')
    .controller('JobCreateController', function($scope, $state, Company, Job, Principal, UserProfile) {
        $scope.$state = $state;
        $scope.save = function () {
            if ($scope.job.id != null) {
                Job.update($scope.job,
                    function (res) {
                        $scope.clear();
                        $scope.$state.go('jobDetail', {id: job.id});
                    });
            } else {
            	$scope.job.company = {
        			id: $scope.companyId
        		};
                
                Job.save($scope.job,	
                    function (res) {
                		$scope.clear();
                        $scope.$state.go('home');
                    });
            }
        };

        Principal.identity().then(function(account) {
        	UserProfile.getByLogin({login: account.login}, function(result) {
        		$scope.companyId = result.company.id;
        		console.log('company id: ' + $scope.companyId);
        	});
        });
        
        $scope.clear = function () {
            $scope.job = {title: null, description: null, responsibilities: null, requirements: null, postDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
