'use strict';

angular.module('boardatjobApp')
    .factory('JobApplication', function ($resource) {
        return $resource('api/jobApplications/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByJobAndLogin': {
            	url: 'api/jobApplications/job/:jobid/login/:login',
            	method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByCompany': {
            	url: 'api/jobApplications/company/:companyId',
            	method: 'GET',
            	isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
