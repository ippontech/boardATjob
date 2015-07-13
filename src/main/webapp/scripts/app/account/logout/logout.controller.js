'use strict';

angular.module('boardatjobApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
