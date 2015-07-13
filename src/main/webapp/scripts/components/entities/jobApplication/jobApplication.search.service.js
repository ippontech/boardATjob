'use strict';

angular.module('boardatjobApp')
    .factory('JobApplicationSearch', function ($resource) {
        return $resource('api/_search/jobApplications/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
