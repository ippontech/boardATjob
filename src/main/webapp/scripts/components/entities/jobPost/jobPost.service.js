'use strict';

angular.module('boardatjobApp')
    .factory('JobPost', function ($resource, DateUtils) {
        return $resource('api/jobPosts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
