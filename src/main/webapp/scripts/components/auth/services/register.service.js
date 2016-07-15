'use strict';

angular.module('socialnetworkApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


