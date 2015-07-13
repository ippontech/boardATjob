'use strict';

angular.module('boardatjobApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userProfile', {
                parent: 'entity',
                url: '/userProfile',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.userProfile.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userProfile/userProfiles.html',
                        controller: 'UserProfileController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userProfile');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userProfileDetail', {
                parent: 'entity',
                url: '/userProfile/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'boardatjobApp.userProfile.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userProfile/userProfile-detail.html',
                        controller: 'UserProfileDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userProfile');
                        return $translate.refresh();
                    }]
                }
            });
    });
