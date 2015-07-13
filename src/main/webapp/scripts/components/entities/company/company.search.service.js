'use strict';

angular.module('boardatjobApp')
    .factory('CompanySearch', function ($resource) {
        return $resource('api/_search/companys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
