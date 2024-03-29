'use strict';

angular.module('socialnetworkApp')
    .factory('Persona', function ($resource, DateUtils) {
        return $resource('api/personas/:id', {}, {
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
