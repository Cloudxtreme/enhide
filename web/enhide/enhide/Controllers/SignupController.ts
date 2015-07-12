module enhide.Controllers {
    "use strict";
    import SignupService = enhide.Services.SignupService;
    import Captcha = enhide.Services.Captcha;


    var app = getModule();

    interface SignupForm extends ng.IFormController {
        retypedPassword: any;
    }

    class SignupController {
        private captchaImage: string;
        private name: string;
        private username: string;
        private password: string;
        private retypedPassword: string;
        private signupForm: SignupForm;
        private solution: string;
        private hash: string;
        constructor(
            private $state: ng.ui.IStateService,
            private signupService: SignupService) {
            this.captcha();
        }

        public signup(): void {
            var self: SignupController = this;
            this.signupService.signup(
                this.name,
                this.username,
                this.password,
                this.hash,
                this.solution
                ).success(function () {
                self.$state.go("login");
            }).error(function (data) {
                console.log(data);
            });
        }

        public captcha(): void {
            var self: SignupController = this;
            this.signupService.captcha().success(function (data: Captcha) {
                self.captchaImage = data.image;
                self.hash = data.hash;
            });
        }

        public passwordsChanged(): void {
            this.signupForm.retypedPassword
                .$setValidity("mismatch", this.password === this.retypedPassword);
        }

        public static $inject: string[] = ["$state", "SignupService"];
    }

    app.controller("SignupController", SignupController);
}
