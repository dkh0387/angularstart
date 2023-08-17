import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
//import {HelloworldModule} from './helloworldcomponent/helloworld.module';


platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
