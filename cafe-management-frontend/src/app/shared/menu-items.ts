import {Injectable} from "@angular/core";
import {GlobalConstants} from "./global-constants";

export interface Menu {
  state: string;
  name: string;
  type: string;
  icon: string;
  role: string;
}

const MENUITEMS = [
  {
    state: GlobalConstants.dashboardPath,
    name: 'Dashboard',
    type: 'link',
    icon: GlobalConstants.dashboardPath,
    role: GlobalConstants.roleAny
  },
  {
    state: GlobalConstants.categoryPath,
    name: 'Manage Category',
    type: 'link',
    icon: GlobalConstants.categoryPath,
    role: GlobalConstants.roleAdmin
  }
];

@Injectable()
export class MenuItems {

  getMenuItem(): Menu[] {
    return MENUITEMS;
  }
}



