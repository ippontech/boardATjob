'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobApplication', {
                parent: 'entity',
                url: '/jobApplication',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.jobApplication.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobApplication/jobApplications.html',
                        controller: 'JobApplicationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jobApplication');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jobApplicationDetail', {
                parent: 'entity',
                url: '/jobApplication/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.jobApplication.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobApplication/jobApplication-detail.html',
                        controller: 'JobApplicationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jobApplication');
                        return $translate.refresh();
                    }]
                }
            })
	        .state('apply', {
	            parent: 'entity',
	            url: '/jobApplicationApply/:jobId/:userLogin',
	            data: {
	                roles: ['ROLE_USER'],
	                pageTitle: 'boardatjobApp.jobApplication.detail.title'
	            },
	            views: {
	                'content@': {
	                    templateUrl: 'scripts/app/entities/jobApplication/jobApplication-apply.html',
	                    controller: 'JobApplicationApplyController'
	                }
	            },
	            resolve: {
	                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
	                    $translatePartialLoader.addPart('jobApplication');
	                    return $translate.refresh();
	                }]
	            }
	        })
	        .state('recruiterJobApplications', {
                parent: 'entity',
                url: '/recruiterJobApplications',
                data: {
                    roles: ['ROLE_RECRUITER'],
                    pageTitle: 'boardatjobApp.jobApplication.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobApplication/recruiterJobApplications.html',
                        controller: 'JobApplicationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jobApplication');
                        return $translate.refresh();
                    }]
                }
            });
    });
