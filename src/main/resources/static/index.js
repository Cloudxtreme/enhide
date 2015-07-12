var enhide;
(function (enhide) {
    "use strict";
    angular.module("enhide", ['ngMaterial', 'ui.router']);
    enhide.getModule = function () {
        return angular.module("enhide");
    };
    enhide.getBaseURL = function () {
        return "http://localhost:8080";
    };
    enhide.getClientID = function () {
        return "clientapp";
    };
    enhide.getClientSecret = function () {
        return "123456";
    };
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        function ErrorInterceptor($q, $injector) {
            return {
                responseError: function (rejection) {
                    console.log(rejection.status);
                    if (rejection.status >= 400 && rejection.status < 600) {
                        $injector.invoke(function ($mdDialog) {
                            $mdDialog.show($mdDialog.alert().title('Error: ' + rejection.data.error).content(rejection.data.error_description).ariaLabel('Error ' + rejection.data.error_description).ok('OK'));
                        });
                    }
                    return $q.reject(rejection);
                }
            };
        }
        Services.ErrorInterceptor = ErrorInterceptor;
        app.config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push(ErrorInterceptor);
        }]);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        var SignupService = (function () {
            function SignupService($http) {
                this.$http = $http;
            }
            SignupService.prototype.signup = function (name, username, password, hash, solution) {
                var config = {
                    method: "POST",
                    url: enhide.getBaseURL() + "/signup",
                    data: {
                        "user": {
                            "name": name,
                            "login": username,
                            "password": password
                        },
                        "captcha": {
                            "hash": hash,
                            "solution": solution
                        }
                    }
                };
                return this.$http(config);
            };
            SignupService.prototype.captcha = function () {
                var config = {
                    method: "GET",
                    url: enhide.getBaseURL() + "/captcha"
                };
                return this.$http(config);
            };
            SignupService.$inject = ["$http"];
            return SignupService;
        })();
        Services.SignupService = SignupService;
        app.service("SignupService", SignupService);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        var TokenService = (function () {
            function TokenService($http) {
                this.$http = $http;
                this.clientID = enhide.getClientID();
                this.clientSecret = enhide.getClientSecret();
                var storedTokenString = sessionStorage.getItem('token');
                if (storedTokenString) {
                    var storedToken = JSON.parse(storedTokenString);
                    this.token = storedToken;
                    $http.defaults.headers.common.Authorization = 'Bearer ' + storedToken.access_token;
                }
            }
            TokenService.prototype.retrieve = function (username, password) {
                return this.retrieveOrRefresh({
                    username: username,
                    password: password,
                    grant_type: "password",
                    scope: "read write",
                    client_id: this.clientID,
                    client_secret: this.clientSecret
                });
            };
            TokenService.prototype.refresh = function () {
                return this.retrieveOrRefresh({
                    grant_type: "refresh_token",
                    refresh_token: this.token.refresh_token,
                    client_id: this.clientID,
                    client_secret: this.clientSecret
                });
            };
            TokenService.prototype.revoke = function () {
                var config = {
                    method: "GET",
                    url: enhide.getBaseURL() + "/revoke",
                };
                var self = this;
                return this.$http(config).then(function () {
                    self.$http.defaults.headers.common.Authorization = "";
                    delete self.$http.defaults.headers.common["Authorization"];
                    sessionStorage.removeItem('token');
                });
            };
            TokenService.prototype.retrieveOrRefresh = function (request) {
                var self = this;
                var authorization = "Basic " + btoa(this.clientID + ":" + this.clientSecret);
                var data = {};
                var config = {
                    method: "POST",
                    url: enhide.getBaseURL() + "/oauth/token",
                    headers: {
                        "Authorization": authorization,
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    data: $.param(request)
                };
                return this.$http(config).then(function (arg) {
                    var data = arg.data;
                    self.$http.defaults.headers.common.Authorization = 'Bearer ' + data.access_token;
                    self.token = data;
                    sessionStorage.setItem('token', JSON.stringify(data));
                    self.refreshHandle = setTimeout(function () {
                        self.refresh();
                    }, parseInt(data.expires_in) * 500);
                    return data;
                });
            };
            TokenService.$inject = ["$http"];
            return TokenService;
        })();
        Services.TokenService = TokenService;
        app.service("TokenService", TokenService);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        var IsService = (function () {
            function IsService($http, $q, tokenService) {
                this.$http = $http;
                this.$q = $q;
                this.tokenService = tokenService;
            }
            IsService.prototype.isAdmin = function () {
                return this.is("admin");
            };
            IsService.prototype.isAuthenticated = function () {
                return this.is("authenticated");
            };
            IsService.prototype.isGuest = function () {
                return this.is("guest");
            };
            IsService.prototype.isUser = function () {
                return this.is("user");
            };
            IsService.prototype.isAnonymous = function () {
                if (!this.$http.defaults.headers.common.Authorization) {
                    return this.$q.when(true);
                }
                else {
                    return this.$q.reject();
                }
            };
            IsService.prototype.is = function (thing) {
                var config = {
                    method: "GET",
                    url: enhide.getBaseURL() + "/is/" + thing,
                };
                return this.$http(config).then(function (response) {
                    return response.status == 200;
                });
            };
            IsService.$inject = ["$http", "$q"];
            return IsService;
        })();
        Services.IsService = IsService;
        app.service("IsService", IsService);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        var ChangePasswordService = (function () {
            function ChangePasswordService($http) {
                this.$http = $http;
            }
            ChangePasswordService.prototype.changePassword = function (newPassword) {
                var config = {
                    method: "PUT",
                    url: enhide.getBaseURL() + "/change_password",
                    data: {
                        "newPassword": newPassword
                    }
                };
                return this.$http(config);
            };
            ChangePasswordService.$inject = ["$http"];
            return ChangePasswordService;
        })();
        Services.ChangePasswordService = ChangePasswordService;
        app.service("ChangePasswordService", ChangePasswordService);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Services;
    (function (Services) {
        "use strict";
        var app = enhide.getModule();
        var GreetingService = (function () {
            function GreetingService($http) {
                this.$http = $http;
            }
            GreetingService.prototype.greeting = function () {
                var config = {
                    method: "GET",
                    url: enhide.getBaseURL() + "/greeting",
                };
                return this.$http(config).then(function (response) {
                    return response.data;
                });
            };
            GreetingService.$inject = ["$http"];
            return GreetingService;
        })();
        Services.GreetingService = GreetingService;
        app.service("GreetingService", GreetingService);
    })(Services = enhide.Services || (enhide.Services = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Controllers;
    (function (Controllers) {
        "use strict";
        var app = enhide.getModule();
        var SignupController = (function () {
            function SignupController($state, signupService) {
                this.$state = $state;
                this.signupService = signupService;
                this.captcha();
            }
            SignupController.prototype.signup = function () {
                var self = this;
                this.signupService.signup(this.name, this.username, this.password, this.hash, this.solution).success(function () {
                    self.$state.go("login");
                }).error(function (data) {
                    console.log(data);
                });
            };
            SignupController.prototype.captcha = function () {
                var self = this;
                this.signupService.captcha().success(function (data) {
                    self.captchaImage = data.image;
                    self.hash = data.hash;
                });
            };
            SignupController.prototype.passwordsChanged = function () {
                this.signupForm.retypedPassword.$setValidity("mismatch", this.password === this.retypedPassword);
            };
            SignupController.$inject = ["$state", "SignupService"];
            return SignupController;
        })();
        app.controller("SignupController", SignupController);
    })(Controllers = enhide.Controllers || (enhide.Controllers = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Controllers;
    (function (Controllers) {
        "use strict";
        var app = enhide.getModule();
        var LoginController = (function () {
            function LoginController($state, tokenService) {
                this.$state = $state;
                this.tokenService = tokenService;
            }
            LoginController.prototype.login = function () {
                var self = this;
                this.tokenService.retrieve(this.email, this.password).then(function (response) {
                    self.$state.go("greeting");
                }, function (reason) {
                    console.error(reason);
                });
            };
            LoginController.$inject = ["$state", "TokenService"];
            return LoginController;
        })();
        app.controller("LoginController", LoginController);
    })(Controllers = enhide.Controllers || (enhide.Controllers = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Controllers;
    (function (Controllers) {
        "use strict";
        var app = enhide.getModule();
        var ChangePasswordController = (function () {
            function ChangePasswordController($state, changePasswordService) {
                this.$state = $state;
                this.changePasswordService = changePasswordService;
            }
            ChangePasswordController.prototype.changePassword = function () {
                var self = this;
                this.changePasswordService.changePassword(this.password).success(function () {
                    self.$state.go("greeting");
                }).error(function (data) {
                    console.log(data);
                });
            };
            ChangePasswordController.prototype.passwordsChanged = function () {
                this.changePasswordForm.retypedPassword.$setValidity("mismatch", this.password === this.retypedPassword);
            };
            ChangePasswordController.$inject = ["$state", "ChangePasswordService"];
            return ChangePasswordController;
        })();
        app.controller("ChangePasswordController", ChangePasswordController);
    })(Controllers = enhide.Controllers || (enhide.Controllers = {}));
})(enhide || (enhide = {}));
var enhide;
(function (enhide) {
    var Controllers;
    (function (Controllers) {
        "use strict";
        var app = enhide.getModule();
        var GreetingController = (function () {
            function GreetingController(greetingService) {
                this.greetingService = greetingService;
                var self = this;
                this.greetingService.greeting().then(function (response) {
                    self.greeting = response.content;
                });
            }
            GreetingController.$inject = ["GreetingService"];
            return GreetingController;
        })();
        app.controller("GreetingController", GreetingController);
    })(Controllers = enhide.Controllers || (enhide.Controllers = {}));
})(enhide || (enhide = {}));
/// <reference path="scripts/typings/angular-ui-router/angular-ui-router.d.ts" />
var enhide;
(function (enhide) {
    "use strict";
    var app = enhide.getModule();
    app.run([
        '$rootScope',
        '$state',
        '$stateParams',
        'TokenService',
        function ($rootScope, $state, $stateParams, tokenService) {
            // It's very handy to add references to $state and $stateParams to the $rootScope
            // so that you can access them from any scope within your applications.For example,
            // <li ng-class="{ active: $state.includes('contacts.list') }"> will set the <li>
            // to active whenever 'contacts.list' or one of its decendents is active.
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]).config([
        '$stateProvider',
        '$urlRouterProvider',
        '$sceDelegateProvider',
        '$httpProvider',
        function ($stateProvider, $urlRouterProvider, $sceDelegateProvider, $httpProvider) {
            $stateProvider.state('login', {
                url: "/login",
                templateUrl: "Views/login.html",
                controller: "LoginController as loginCtrl",
                resolve: {
                    "isAllowed": ['IsService', function (isService) {
                        return isService.isAnonymous();
                    }]
                }
            }).state('signup', {
                url: "/signup",
                templateUrl: "Views/signup.html",
                controller: "SignupController as signupCtrl",
                resolve: {
                    "isAllowed": ['IsService', function (isService) {
                        return isService.isAnonymous();
                    }]
                }
            }).state('change_password', {
                url: "/change_password",
                templateUrl: "Views/change_password.html",
                controller: "ChangePasswordController as changePWCtrl",
                resolve: {
                    "isAllowed": ['IsService', function (isService) {
                        return isService.isAuthenticated();
                    }]
                }
            }).state('greeting', {
                url: "/greeting",
                templateUrl: "Views/greeting.html",
                controller: "GreetingController as greetingCtrl",
                resolve: {
                    "isAllowed": ['IsService', function (isService) {
                        return isService.isAuthenticated();
                    }]
                }
            });
            $urlRouterProvider.otherwise("/login");
        }
    ]);
})(enhide || (enhide = {}));
/// <reference path="index.ts" />
/// <reference path="services/errorinterceptor.ts" />
/// <reference path="services/signupservice.ts" />
/// <reference path="services/tokenservice.ts" />
/// <reference path="services/isservice.ts" />
/// <reference path="services/changepasswordservice.ts" />
/// <reference path="services/greetingservice.ts" />
/// <reference path="controllers/signupcontroller.ts" />
/// <reference path="controllers/logincontroller.ts" />
/// <reference path="controllers/changepasswordcontroller.ts" />
/// <reference path="controllers/greetingcontroller.ts" />
/// <reference path="routes.ts" />
//# sourceMappingURL=index.js.map