'use strict';

angular.module('boardatjobApp')
    .factory('UserProfile', function ($resource) {
        return $resource('api/userProfiles/:id', {}, {
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
