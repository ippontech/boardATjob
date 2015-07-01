'use strict';

angular.module('boardatjobApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
            saveRecruiter: {
                method: 'POST',
                url: 'api/register_recruiter'
            }
        });
    });


