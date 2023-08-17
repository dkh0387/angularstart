import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HelloworldRoutingModule } from './helloworld-routing.module';
import { HelloworldComponent } from './helloworld.component';

@NgModule({
  declarations: [
    HelloworldComponent
  ],
  imports: [
    BrowserModule,
    HelloworldRoutingModule
  ],
  providers: [],
  bootstrap: [HelloworldComponent]
})
/*
NOTE: HelloworldModule has to be imported in main.ts!
 */
export class HelloworldModule { }
