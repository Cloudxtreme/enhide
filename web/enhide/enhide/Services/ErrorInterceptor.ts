module enhide.Services {
    "use strict";

    var app = getModule();

    interface ErrorResponse {
        error: string;
        error_description: string;
        url?: string;
        stackTrace?: [string];
    }

    export function ErrorInterceptor($q: ng.IQService, $injector: ng.auto.IInjectorService) {
        return {
            responseError(rejection: ng.IHttpPromiseCallbackArg<ErrorResponse>) {
                console.log(rejection.status);
                if (rejection.status >= 400 && rejection.status < 600) {
                    $injector.invoke(function ($mdDialog: any) {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .title('Error: ' + rejection.data.error)
                                .content(rejection.data.error_description)
                                .ariaLabel('Error ' + rejection.data.error_description)
                                .ok('OK')
                            );
                    });
                }
                return $q.reject(rejection);
            }
        }
    }

    app.config(['$httpProvider', function ($httpProvider: ng.IHttpProvider) {
        $httpProvider.interceptors.push(ErrorInterceptor);
    }])
}