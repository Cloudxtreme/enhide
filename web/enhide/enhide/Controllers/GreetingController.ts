module enhide.Controllers {
    "use strict";
    import GreetingService = enhide.Services.GreetingService;
    import GreetingResponse = enhide.Services.GreetingResponse;


    var app = getModule();

    class GreetingController {
        private greeting: string;
        constructor(
            private greetingService: GreetingService
            ) {
            var self: GreetingController = this;
            this.greetingService.greeting().then(function (response: GreetingResponse) {
                self.greeting = response.content;
            });
        }
        public static $inject: string[] = ["GreetingService"];
    }

    app.controller("GreetingController", GreetingController);
}