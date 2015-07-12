module enhide.Services {
    "use strict";

    var app = getModule();

    export interface Captcha {
        image: string;
        hash: string;
    }

    export class SignupService {
        constructor(private $http: ng.IHttpService) {
        }

        public signup(
            name: string,
            username: string,
            password: string,
            hash: string,
            solution: string): ng.IHttpPromise<{}> {
            var config : ng.IRequestConfig = <ng.IRequestConfig>{
                method: "POST",
                url: getBaseURL() + "/signup",
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
        }

        public captcha(): ng.IHttpPromise<Captcha> {
            var config = {
                method: "GET",
                url: getBaseURL() + "/captcha"
            };

            return this.$http(config);
        }

        public static $inject: string[] = ["$http"];
    }

    app.service("SignupService", SignupService);
}