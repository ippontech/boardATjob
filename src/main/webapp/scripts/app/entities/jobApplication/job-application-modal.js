'use strict';

angular.module('boardatjobApp')
    .config(function($stateProvider) {
        var onApply = function ($stateParams, $modal) {
            $modal.open({
                templateUrl: 'scripts/app/entities/jobApplication/job-application-modal.html',
                controller: 'JobApplicationModalController',
                keyboard: true,
                resolve: {
                    jobId: function() {
                        return $stateParams.id;
                    },
                    currentUser: function(Principal) {
                        return Principal.identity(true).then(function(account) {
                            return account;
                        })
                    }
                }
            })
        };

        $stateProvider
            .state('applyForJob', {
                parent: 'jobDetail',
                url: '',
                params: {
                    id: null
                },
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: onApply
            });
    });
