angular.module('boardatjobApp')
    .controller('JobCreateController', function($scope, $state, $stateParams, Company, Job, Principal, UserProfile) {
        $scope.$state = $state;
        
        $scope.load = function(jobId) {
        	Job.get({id: jobId}, function(result) {
        		$scope.job = result;
        	});
        };
        console.log($stateParams.id);
        if ($stateParams.id) {
        	$scope.load($stateParams.id);
        }
        
        $scope.save = function () {
            if ($scope.job.id != null) {
                Job.update($scope.job,
                    function (res) {
                        $scope.success = 'OK';
                        setTimeout(function() {
                        	$scope.success = null;
                        }, 3000);
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
