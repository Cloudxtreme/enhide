module enhide {
    "use strict";
    angular.module("enhide", ['ngMaterial', 'ui.router']);

    export var getModule: () => ng.IModule = () => {
        return angular.module("enhide");
    }

    export var getBaseURL: () => string = () => {
        return "http://localhost:8080";
    }

    export var getClientID: () => string = () => {
        return "clientapp";
    }

    export var getClientSecret: () => string = () => {
        return "123456";
    }
}