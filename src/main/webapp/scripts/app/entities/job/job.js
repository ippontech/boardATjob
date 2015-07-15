'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('job', {
                parent: 'entity',
                url: '/job',
                data: {
                    roles: [],
                    pageTitle: 'boardatjobApp.job.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/job/jobs.html',
                        controller: 'JobController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('job');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jobDetail', {
                parent: 'entity',
                url: '/job/:id',
                data: {
                    roles: [],
                    pageTitle: 'boardatjobApp.job.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/job/job-detail.html',
                        controller: 'JobDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('job');
                        return $translate.refresh();
                    }]
                }
            });
    });
