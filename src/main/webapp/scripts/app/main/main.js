'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
            	name: 'home',
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
                    'jobList@home': {
                        templateUrl: 'scripts/app/entities/job/jobList.html',
                        controller: 'JobController'

                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        $translatePartialLoader.addPart('jobApplication');
                        return $translate.refresh();
                    }]
                }
            });
    });
