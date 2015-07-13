'use strict';

angular.module('boardatjobApp')
    .factory('JobSearch', function ($resource) {
        return $resource('api/_search/jobs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
