'use strict';

angular.module('boardatjobApp')
    .factory('UserProfileSearch', function ($resource) {
        return $resource('api/_search/userProfiles/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
