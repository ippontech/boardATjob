'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobPost', {
                parent: 'entity',
                url: '/jobPost',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.jobPost.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobPost/jobPosts.html',
                        controller: 'JobPostController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jobPost');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jobPostDetail', {
                parent: 'entity',
                url: '/jobPost/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.jobPost.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobPost/jobPost-detail.html',
                        controller: 'JobPostDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jobPost');
                        return $translate.refresh();
                    }]
                }
            });
    });
