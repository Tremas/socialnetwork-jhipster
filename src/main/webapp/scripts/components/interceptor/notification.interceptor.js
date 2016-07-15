 'use strict';

angular.module('socialnetworkApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-socialnetworkApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-socialnetworkApp-params')});
                }
                return response;
            }
        };
    });
