angular.module('boardatjobApp').controller('HeaderController',
	function($rootScope, $scope, $state, Principal, UserProfile) {
		$scope.isAuthenticated = Principal.isAuthenticated;
		$scope.company = null;
	
		$scope.createJob = function() {
			if ($scope.company && $scope.company.id) {
				$state.go('jobCreate');
			} else {
				$state.go('mycompany');
			}
		};
	
		Principal.identity().then(function(account) {
			UserProfile.getByLogin({
				login : account.login
			}, function(result) {
				$scope.company = result.company;
				console.log('company id: ' + $scope.company.id);
	
			});
		});
	});
