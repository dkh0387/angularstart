import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AboutMeRoutes } from './about-me.routing';
import { MaterialModule } from '../shared/material-module';
import {AboutMeComponent} from "./about-me.component";

@NgModule({
  imports: [
    CommonModule,
    MaterialModule,
    FlexLayoutModule,
    RouterModule.forChild(AboutMeRoutes)
  ],
  declarations: [AboutMeComponent]
})
export class AboutMeModule { }
