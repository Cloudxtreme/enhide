module enhide.Services {
    "use strict";

    var app = getModule();

    export interface TokenResponse {
        access_token: string;
        token_type: string;
        refresh_token: string;
        expires_in: string;
        scope: string;
    }

    interface TokenError {
        error: string;
        error_description: string;
    }

    interface TokenRequest {
        username?: string;
        password?: string;
        refresh_token?: string;
        grant_type: string;
        scope?: string;
        client_id: string;
        client_secret: string;
    }

    export class TokenService {
        private token: TokenResponse;
        private refreshHandle: number;
        private clientID: string;
        private clientSecret: string;
        constructor(private $http: ng.IHttpService) {
            this.clientID = getClientID();
            this.clientSecret = getClientSecret();
            var storedTokenString : string = sessionStorage.getItem('token');
            if (storedTokenString) {
                var storedToken : TokenResponse = JSON.parse(storedTokenString);
                this.token = storedToken;
                $http.defaults.headers.common.Authorization = 'Bearer ' + storedToken.access_token;
            }
        }

        public retrieve(username: string, password: string): ng.IPromise<TokenResponse> {
            return this.retrieveOrRefresh({
                username: username,
                password: password,
                grant_type: "password",
                scope: "read write",
                client_id: this.clientID,
                client_secret: this.clientSecret
            });
        }

        public refresh(): ng.IPromise<TokenResponse> {
            return this.retrieveOrRefresh({
                grant_type: "refresh_token",
                refresh_token: this.token.refresh_token,
                client_id: this.clientID,
                client_secret: this.clientSecret
            });
        }

        public revoke(): ng.IPromise<void> {
            var config: ng.IRequestConfig = {
                method: "GET",
                url: getBaseURL() + "/revoke",
            };

            var self: TokenService = this;
            return this.$http(config).then(function () {
                self.$http.defaults.headers.common.Authorization = "";
                delete self.$http.defaults.headers.common["Authorization"];
                sessionStorage.removeItem('token');
            });
        }

        private retrieveOrRefresh(request: TokenRequest): ng.IPromise<TokenResponse> {
            var self: TokenService = this;
            var authorization: string = "Basic " + btoa(this.clientID + ":" + this.clientSecret);
            var data: any = {};
            var config: ng.IRequestConfig = {
                method: "POST",
                url: getBaseURL() + "/oauth/token",
                headers: {
                    "Authorization": authorization,
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                data: $.param(request)
            };
            return this.$http(config).then(function (arg: ng.IHttpPromiseCallbackArg<TokenResponse>) {
                var data: TokenResponse = arg.data;
                self.$http.defaults.headers.common.Authorization = 'Bearer ' + data.access_token;
                self.token = data;
                sessionStorage.setItem('token', JSON.stringify(data));
                self.refreshHandle = setTimeout(function () {
                    self.refresh();
                }, parseInt(data.expires_in) * 500);
                return data;
            });
        }

        public static $inject: string[] = ["$http"];
    }

    app.service("TokenService", TokenService);
} 