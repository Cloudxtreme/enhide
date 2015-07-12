module enhide.Controllers {
    "use strict";
    import ChangePasswordService = enhide.Services.ChangePasswordService;

    var app = getModule();

    interface ChangePasswordForm extends ng.IFormController {
        retypedPassword: any;
    }

    class ChangePasswordController {
        private password: string;
        private retypedPassword: string;
        private changePasswordForm: ChangePasswordForm;
        constructor(
            private $state: ng.ui.IStateService,
            private changePasswordService: ChangePasswordService) {
        }

        public changePassword(): void {
            var self: ChangePasswordController = this;
            this.changePasswordService.changePassword(
                this.password
                ).success(function () {
                self.$state.go("greeting");
            }).error(function (data) {
                console.log(data);
            });
        }

        public passwordsChanged(): void {
            this.changePasswordForm.retypedPassword
                .$setValidity("mismatch", this.password === this.retypedPassword);
        }

        public static $inject: string[] = ["$state", "ChangePasswordService"];
    }

    app.controller("ChangePasswordController", ChangePasswordController);
}