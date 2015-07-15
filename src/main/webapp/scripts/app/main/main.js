'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    },
                    'applicants@home': {
                        templateUrl: 'scripts/app/entities/jobApplication/jobApplications.html',
                        controller: 'JobApplicationController'
                    },
                    'jobSearch@home': {
                        templateUrl: 'scripts/app/entities/job/jobSearch.html',
                        controller: 'JobController'
                    }
               },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
