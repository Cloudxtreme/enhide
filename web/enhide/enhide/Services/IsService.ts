module enhide.Services {
    "use strict";

    var app = getModule();

    export class IsService {
        constructor(
            private $http: ng.IHttpService,
            private $q: ng.IQService,
            private tokenService: TokenService
            ) {
        }

        public isAdmin(): ng.IPromise<boolean> {
            return this.is("admin");
        }

        public isAuthenticated(): ng.IPromise<boolean> {
            return this.is("authenticated");
        }

        public isGuest(): ng.IPromise<boolean> {
            return this.is("guest");
        }

        public isUser(): ng.IPromise<boolean> {
            return this.is("user");
        }

        public isAnonymous(): ng.IPromise<boolean> {
            if (
                !this.$http.defaults.headers.common.Authorization
                ) {
                return this.$q.when(true);
            } else {
                return this.$q.reject();
            }
        }

        private is(thing: string): ng.IPromise<boolean> {
            var config: ng.IRequestConfig = <ng.IRequestConfig>{
                method: "GET",
                url: getBaseURL() + "/is/" + thing,
            }

            return this.$http(config).then(function (response: ng.IHttpPromiseCallbackArg<{}>) {
                return response.status == 200;
            });
        }

        public static $inject: string[] = ["$http", "$q"];
    }

    app.service("IsService", IsService);
}