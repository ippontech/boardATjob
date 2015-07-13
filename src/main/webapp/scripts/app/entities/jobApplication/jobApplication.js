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
            });
    });
