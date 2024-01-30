# MyFirstProject

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.0.

1. Start the app:

   Run `ng serve --open --port 5100` in terminal in order to start the app.

2. Generating a new component:

   Run `ng g component header` in terminal in order to create a new angular component.

3. Generating a new service:

   Run `ng g service friendsAddingService` in terminal to create a new angular component.

4. Project structure (http://www.angular.io/guide/file-struckture):

    - `angular.json`: Angular workspace configuration, list of execution targets
    - `node_module`: local repo of node modules
    - `project.json`: project meta data, list of node dependencies like `pom.xml`
    - `src/app`: app components, templates, etc.
    - `src/assets`: resources like images
    - `src/environments`: profiles for environments like dev, prod, test, etc.; analogon to spring profiles
    - `src/index.html`: main launch page
    - `src/polyfills.ts`: add supports for different browser versions
    - `src/test.ts`: unit test cases
    - `src/tsconfig.json`: TypeScript compiler configs

5. Angular install guide:

    - Install `npm` (package manager)
    - Install `nodeJS` (JavaScript runtime)
    - Install Angular CLI: `npm install --location=global @angular/cli@14.0.7` (version number at the end)
    - Create a new project: `ng new --no-standalone my-first-project`

6. App loading:
   The app entry point when the app is starting is `<app-root></app-root>` in `index.html`
7. 

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you
change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also
use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a
package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out
the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
