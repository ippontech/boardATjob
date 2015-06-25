'use strict';

angular.module('boardatjobApp')
    .factory('Company', function ($resource, DateUtils) {
        return $resource('api/companys/:id', {}, {
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
