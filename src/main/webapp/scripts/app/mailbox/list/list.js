'use strict';

angular.module('msapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.messages', {
                url: '/:directory',
                data: {
                    authorities: ['CASE_WORKER', 'FOSTER_PARENT'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/list/list.html',
                        controller: 'MessagesCtrl'
                    }
                },
                resolve: {
                    filterByDestination: ['$state', function($state) {
                        return $state.params.contact;
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            });
    });
