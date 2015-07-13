'use strict';

angular.module('boardatjobApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


