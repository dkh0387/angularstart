# Frontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.2.9.

## CSS design stuff

* Can be found here: `src/assets`
* Themes etc.: `src/assets/styles/variable.scss`

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
*

### Teststatus:

* Components:
  * `login.component.ts` OK
  * `category.component.ts` OK
  * `manage-category.component.ts` OK

* Services:
  * `dashboard.service` OK
  * `route-guard.service` OK
  * `category.service.ts` OK

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

The whole concept of app routing is defined in `app-routing.module.ts`.
We do have two main routes:

* Before login (home page). Here we do render `home.component.html` (landing page)
* After login (``/cafe/...) routes, for example dashboard). Here we do render `full.component.html` (with user change password icon and sidebar menu).

## Component-based routing

WE are able to define routing access per component. As example, we use `material.routing.ts`:

* Basic path 'category/...'
* For that, we define the corresponding component
* Access restrictions: admin only, checked by `route-guard.service.ts`

## Providing JWT Authentication

* We can check if a token is stored in local storage by right click-Application (toolbar on the top)-Local storage in the left sidebar
* We implement a JWT decoder (see `route-guard.service.ts`)
* We implement a token HTTP interceptor (see `token.interceptor.ts`)
* This interceptor provides a Bearer Authentication header to any HTTP request to the backend using JWT
* We inject the `TokenInterceptor` to `app.module.ts`

## Example of a simple modal dialog with fields with sending data to a backend endpoint

* see: `change-password.component.ts`

## Concept of using modal dialogs incl. data binding (example)

* We create a modal dialog instance in `header.component.ts`
* We create a dialog content component in `confirmation.component.ts`
* Detection of data changes by using `EventEmitter`
* Data binding: Injection of dialog frame using `@Inject(MAT_DIALOG_DATA) public dialogData` in the content component
* All data from dialog frame are bound to the `confirmation.component.html`

## Example of using data tables

* see `manage-category.component.html`

## Example of using multi selection options:

* see `product.component.html`

## Adding a new sidebar menu element (on category management example)

* Create a new service for endpoint calls: `category.service.ts`
* Create a new component to execute the calls from service (CRUD). This one is a dialog content: `category.component.ts`
* Create a new component for the page itself (by clicking on the sidebar menu item this one appears): `manage-category.component.ts`.
  Here we create buttons to calls dialogs with the component inside
* Add the new menu item to the list: `menu-items.ts`

## Role management

* Resources are bound to the according user role of a logged-in user
* Which urls are allowed to be reached for which role is described via routing concept (see above)
* Further, we can provide an expected role to any UI element (like sidebar icons): see `sidebar.component.ts` for example
