# Frontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.2.9.

## CSS design stuff

* Can be found here: `src/assets`
* Themes etc.: `src/assets/styles/variable.scss`
* `NgxUiLoaderConfig`: see `app.module.ts`

## Translation

* Details: `https://www.codeandweb.com/babeledit/tutorials/how-to-translate-your-angular-app-with-ngx-translate`
* Default language settings in `app.component.ts ` using `translate.setDefaultLang('en')`. The default language is the
  fall-back language, that is used if a translation cannot be found.
  Set the current language to English by `calling translate.use('en')`.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change
any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also
use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Installing/Updating dependencies

* go to: `package.json`
* bei major updates (like Angular 1x.x) the best wy is to hardly remove the `node_modules` folder and
  the `package-lock.json` file.
  To do so we need:
  - `sudo rm -rf .angular/cache `
  - `sudo rm -rf node_modules/`
  - remove `package-lock.json`
  - run `sudo npm install` again to rebuild the dependency tree

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag
for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out
the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Troubleshooting

In case of the error "error:0308010C:digital envelope routines::unsupported" (doi to the V18 of Node.js) we need to
enable the OpenSSL legacy provider:

`export NODE_OPTIONS=--openssl-legacy-provider`

See https://stackoverflow.com/questions/69692842/error-message-error0308010cdigital-envelope-routinesunsupported for
more details.

## Testing

For details see: https://medium.com/swlh/angular-unit-testing-jasmine-karma-step-by-step-e3376d110ab4

* `jasmine-core` *(develop tests)* Jasmine is the framework we are going to use to create our tests. It has a bunch of
  functionalities to allow us the write different kinds of tests.
* `karma` *(run tests)* Karma is a task runner for our tests. It uses a configuration file in order to set the startup
  file, the reporters, the testing framework, the browser among other things.

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

* JWT will be stored in the local storage after triggering a login endpoint from the back end (for details
  see `login.component.ts` `subscribe()`)
* We can verify JWT by going to the browser: inspect->Application->Local storage
* JWT is being encoded in `route-guard.service.ts`. This `canActivate()` method is a key method for access the app
  components
* After JWT is encoded and role verified, we redirect to the dashboard component
* Dashboard URL is only accessible, if `canActivate()` returned true. This logic is configured
  in `app-routing.module.ts`
* Access to backend endpoints using JWT:
  * After JWT is saved in the local storage, we clone a request to the according backend endpoint and
    add `Bearer <token>` (see `token.interceptor.ts`)

## Global app routing

The whole concept of app routing is defined in `app-routing.module.ts`.
We do have two main routes:

* Before login (home page). Here we do render `home.component.html` (landing page)
* After login (``/cafe/...) routes, for example dashboard). Here we do render `full.component.html` (with user change
  password icon and sidebar menu).

## Component-based routing

We are able to define routing access per component. As example, we use `material.routing.ts`:

* Basic path 'category/...'
* For that, we define the corresponding component
* Access restrictions: admin only, checked by `route-guard.service.ts`

## Providing parameters between components using service

We can provide params from A to B by `router.navigate(...)` using a service.
It has to be listed in `app.module.ts` under `providers` and needs setter/getter for the params.
After that, we need to inject the service into the receiver component.
For a full example see `paypal.service.ts`, `confirm-payment.component.ts`.

## Providing JWT Authentication

* We can check if a token is stored in local storage by right click-Application (toolbar on the top)-Local storage in
  the left sidebar
* We implement a JWT decoder (see `route-guard.service.ts`)
* We implement a token HTTP interceptor (see `token.interceptor.ts`)
* This interceptor provides a Bearer Authentication header to any HTTP request to the backend using JWT
* We inject the `TokenInterceptor` to `app.module.ts`

## Example of a simple modal dialog with fields with sending data to a backend endpoint

* See: `change-password.component.ts`

## Concept of using modal dialogs incl. Data binding (example)

* We create a modal dialog instance in `header.component.ts`
* We create a dialog content component in `confirmation.component.ts`
* Detection of data changes by using `EventEmitter`
* Data binding: Injection of dialog frame using `@Inject(MAT_DIALOG_DATA) public dialogData` in the content component
* All data from dialog frame are bound to the `confirmation.component.html`

## Example of using data tables

* See `manage-category.component.html`

## Example of using multi selection options included data bindings:

* See `product.component.html`
* Event-based selection (get product by selected category) see: `manage-order.component.html`

## Adding a new sidebar menu element (on category management example)

* Create a new service for endpoint calls: `category.service.ts`
* Create a new component to execute the calls from service (CRUD). This one is a dialog content: `category.component.ts`
* Create a new component for the page itself (by clicking on the sidebar menu item this one
  appears): `manage-category.component.ts`.
  Here we create buttons to calls dialogs with the component inside
* Add the new menu item to the list: `menu-items.ts`
* NOTE: the connection between URL path and according component being rendered has to be implemented in the according
  routing module: `material.routing.ts`

## Role management

* Resources are bound to the according user role of a logged-in user
* Which urls are allowed to be reached for which role is described via routing concept (see above)
* Further, we can provide an expected role to any UI element (like sidebar icons): see `sidebar.component.ts` for
  example

## PayPal API integration

* Get started: `https://developer.paypal.com/api/rest/`
* Insert PayPal script into `index.html`
*

## TODOs

* PayPal API integration complete DONE
* Payment confirmation page template (download link for dokument, document object providing over `DocumentService`, back button action, download invoice action)
* PayPal button remove from buy dialog and replace with "add to cart"
* Shopping cart component with PayPal and Tinkoff buttons
* Juristic stuff in buy dialog??
* Tinkoff integration: `https://www.tinkoff.ru/kassa/dev/payments/#section/Vvedenie/Sposoby-integracii`
* Link to GoogleDrive send per Email after payment
* Lena:
  * documents descriptions, names and urls (if multiple)
  * Title photo, title text
  * Links for social media
  * Text for block 3
* Installing dependencies save way: `sudo npm i <packagename> --legacy-peer-deps` --> update to angular 16
* testing
