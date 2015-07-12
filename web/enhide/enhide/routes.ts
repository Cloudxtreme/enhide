/// <reference path="scripts/typings/angular-ui-router/angular-ui-router.d.ts" />
module enhide {
    "use strict";
    import TokenService = enhide.Services.TokenService;
    import IsService = enhide.Services.IsService;

    interface ExtendedRootScope {
        $state: ng.ui.IState;
        $stateParams: ng.ui.IStateParamsService;
    }

    var app = getModule();
    app.run(
        [
            '$rootScope',
            '$state',
            '$stateParams',
            'TokenService',
            function (
                $rootScope: ExtendedRootScope,
                $state: ng.ui.IState,
                $stateParams: ng.ui.IStateParamsService,
                tokenService: TokenService) {

                // It's very handy to add references to $state and $stateParams to the $rootScope
                // so that you can access them from any scope within your applications.For example,
                // <li ng-class="{ active: $state.includes('contacts.list') }"> will set the <li>
                // to active whenever 'contacts.list' or one of its decendents is active.
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ])
        .config(
        [
            '$stateProvider',
            '$urlRouterProvider',
            '$sceDelegateProvider',
            '$httpProvider',
            function (
                $stateProvider: ng.ui.IStateProvider,
                $urlRouterProvider: ng.ui.IUrlRouterProvider,
                $sceDelegateProvider: ng.ISCEDelegateProvider,
                $httpProvider: ng.IHttpProvider) {
                $stateProvider
                    .state('login', <ng.ui.IState>{
                    url: "/login",
                    templateUrl: "Views/login.html",
                    controller: "LoginController as loginCtrl",
                    resolve: {
                        "isAllowed": ['IsService', function (isService: IsService) {
                            return isService.isAnonymous();
                        }]
                    }
                })
                    .state('signup', <ng.ui.IState>{
                    url: "/signup",
                    templateUrl: "Views/signup.html",
                    controller: "SignupController as signupCtrl",
                    resolve: {
                        "isAllowed": ['IsService', function (isService: IsService) {
                            return isService.isAnonymous();
                        }]
                    }
                })
                    .state('change_password', <ng.ui.IState>{
                    url: "/change_password",
                    templateUrl: "Views/change_password.html",
                    controller: "ChangePasswordController as changePWCtrl",
                    resolve: {
                        "isAllowed": ['IsService', function (isService: IsService) {
                            return isService.isAuthenticated();
                        }]
                    }
                })
                    .state('greeting', <ng.ui.IState>{
                    url: "/greeting",
                    templateUrl: "Views/greeting.html",
                    controller: "GreetingController as greetingCtrl",
                    resolve: {
                        "isAllowed": ['IsService', function (isService: IsService) {
                            return isService.isAuthenticated();
                        }]
                    }
                });

                $urlRouterProvider.otherwise("/login");
            }
        ]);
} 