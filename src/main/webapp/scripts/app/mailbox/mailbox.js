'use strict';

angular.module('msapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox', {
                parent: 'site',
                url: '/mail',
                data: {
                    authorities: ['CASE_WORKER', 'FOSTER_PARENT'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/mailbox/mailbox.html'
                    },
                    'aside@ch-inbox': {
                        templateUrl: 'scripts/app/mailbox/mailbox-aside-nav.html',
                        controller: 'MailBoxCtrl'
                    },
                    'mobile@ch-inbox': {
                        templateUrl: 'scripts/app/mailbox/mailbox-mobile-nav.html',
                        controller: 'MailBoxCtrl'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailBox');
                        return $translate.refresh();
                    }]
                }
            });
    });
