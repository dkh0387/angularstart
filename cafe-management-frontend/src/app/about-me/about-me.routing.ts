import {Routes} from '@angular/router';
import {AboutMeComponent} from "./about-me.component";
import {GlobalConstants} from "../shared/global-constants";

export const AboutMeRoutes: Routes = [{
  path: GlobalConstants.aboutMePath,
  component: AboutMeComponent
}];
