module enhide.Services {
    "use strict";

    var app = getModule();

    export interface GreetingResponse {
        id: number;
        content: string;
    }

    export class GreetingService {
        constructor(private $http: ng.IHttpService) {
        }

        public greeting() : ng.IPromise<GreetingResponse> {
            var config: ng.IRequestConfig = <ng.IRequestConfig>{
                method: "GET",
                url: getBaseURL() + "/greeting",
            }

            return this.$http(config).then(function (response: ng.IHttpPromiseCallbackArg<GreetingResponse>) {
                return response.data;
            });
        }

        public static $inject: string[] = ["$http"];
    }

    app.service("GreetingService", GreetingService);
}