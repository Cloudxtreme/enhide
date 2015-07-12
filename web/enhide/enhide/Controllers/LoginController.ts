module enhide.Controllers {
    "use strict";
    import TokenService = enhide.Services.TokenService;
    import TokenResponse = enhide.Services.TokenResponse;


    var app = getModule();

    class LoginController {
        private email: string;
        private password: string;
        constructor(
            private $state: ng.ui.IStateService,
            private tokenService: TokenService) {
        }

        public login(): void {
            var self: LoginController = this;
            this.tokenService.retrieve(this.email, this.password)
                .then(function (response: TokenResponse) {
                self.$state.go("greeting");
            }, function (reason: any) {
                    console.error(reason);
                });
        }

        public static $inject: string[] = ["$state", "TokenService"];
    }

    app.controller("LoginController", LoginController);
}