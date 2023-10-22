# Frontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.2.9.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Troubleshooting

In case of the error "error:0308010C:digital envelope routines::unsupported" (doi to the V18 of Node.js) we need to enable the OpenSSL legacy provider:

`export NODE_OPTIONS=--openssl-legacy-provider`

See https://stackoverflow.com/questions/69692842/error-message-error0308010cdigital-envelope-routinesunsupported for more details.

## Testing

For details see: https://medium.com/swlh/angular-unit-testing-jasmine-karma-step-by-step-e3376d110ab4

* `jasmine-core` *(develop tests)* Jasmine is the framework we are going to use to create our tests. It has a bunch of functionalities to allow us the write different kinds of tests.
* `karma` *(run tests)* Karma is a task runner for our tests. It uses a configuration file in order to set the startup file, the reporters, the testing framework, the browser among other things.

### Examples:

* Testing a component: `login.component.spec.ts`
* Testing a service: `dashboard.service.spec.ts`

## Debugging

- Start the app locally bei running `Angular CLI Server` (`ng serve`)
- Debug `Angular Application` (in Edit/Run Configurations under JavaScript Debug)

## Login process (JWT saving)

* JWT will be stored in the local storage after triggering a login endpoint from the back end (for details see `login.component.ts` `subscribe()`)
* We can verify JWT by going to the browser: inspect->Application->Local storage
* JWT is being encoded in `route-guard.service.ts`. This `canActivate()` method is a key method for access the app components
* After JWT is encoded and role verified, we redirect to the dashboard component
* Dashboard URL is only accessible, if `canActivate()` returned true. This logic is configured in `app-routing.module.ts`
* Access to backend endpoints using JWT:
  * After JWT is saved in the local storage, we clone a request to the according backend endpoint and add `Bearer <token>` (see `token.interceptor.ts`)

## Global app routing

Defined in `app-routing.module.ts`

## Providing JWT Authentication

* We implement a JWT decoder (see `route-guard.service.ts`)
* We implement a token HTTP interceptor (see `token.interceptor.ts`)
* This interceptor provides a Bearer Authentication header to any HTTP request to the backend using JWT
* We inject the `TokenInterceptor` to `app.module.ts`
