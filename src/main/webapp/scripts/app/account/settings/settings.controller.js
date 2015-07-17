'use strict';

angular.module('boardatjobApp')
    .controller('SettingsController', function ($rootScope, $scope, Principal, Auth, Language, $translate, UserProfile, Upload) {
        $scope.success = null;
        $scope.error = null;
        $scope.uploadSuccess = null;
        $scope.uploadError = null;
        
        $scope.profile = null;
        $scope.resume = null;
        
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
            UserProfile.getByLogin({login: account.login}, function(result) {
            	console.log('Here.. Loaded profile: ',result);
                $scope.profile = result;
                
            });
        });

        
        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
                //console.log('saving profile ', $scope.profile);
                //UserProfile.update($scope.profile);
                // refresh profile
                UserProfile.getByLogin({login: $scope.settingsAccount.login}, function(result) {
                	console.log('Here.. refreshed profile: ',result);
                    $scope.profile = result;
                    
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
        
        $scope.$watch('resume', function () {
            $scope.upload($scope.resume);
        });
        
        $scope.upload = function (files) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    Upload.upload({
                        url: '/resume/upload/' + $scope.profile.id,
                        file: file
                    }).progress(function (evt) {
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                    }).success(function (data, status, headers, config) {
                    	$scope.uploadSuccess = 'file ' + config.file.name + ' uploaded.';
                        $scope.clearSuccessMessage();
                        console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
                    }).error(function (data, status, headers, config) {
                    	$scope.uploadError = 'Upload Failed: ' + data.message;
                        console.log('error status: ' + status, data);
                    });
                }
            }
        };
       
        $scope.uploadDate = function() {
        	if ($scope.profile) {
    	        return moment($scope.profile.resumeDate).format('MMMM Do YYYY, h:mm:ss a');	
        	}
        	return null;
        };

        
        $scope.showResume = function() {
        	return $rootScope.isJobSeeker && $scope.profile && $scope.profile.id != null;
        };
        
        
        $scope.clearSuccessMessage = function() {
        	setTimeout(function() {
        		$scope.uploadSuccess = null;
        	},2000);
        };
    });
