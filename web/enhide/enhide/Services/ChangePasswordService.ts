module enhide.Services {
    "use strict";

    var app = getModule();

    export class ChangePasswordService {
        constructor(private $http: ng.IHttpService) {
        }

        public changePassword(newPassword: string) : ng.IHttpPromise<{}> {
            var config : ng.IRequestConfig = <ng.IRequestConfig>{
                method: "PUT",
                url: getBaseURL() + "/change_password",
                data: {
                    "newPassword": newPassword
                }
            };

            return this.$http(config);
        }

        public static $inject: string[] = ["$http"];
    }

    app.service("ChangePasswordService", ChangePasswordService);
}