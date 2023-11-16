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
    name: GlobalConstants.sidebarDashboard,
    type: 'link',
    icon: GlobalConstants.dashboardPath,
    role: GlobalConstants.roleAny
  },
  {
    state: GlobalConstants.categoryPath,
    name: GlobalConstants.sidebarManageCategory,
    type: 'link',
    icon: GlobalConstants.categoryPath,
    role: GlobalConstants.roleAdmin
  },
  {
    state: GlobalConstants.productPath,
    name: GlobalConstants.sidebarManageProduct,
    type: 'link',
    icon: GlobalConstants.productIcon,
    role: GlobalConstants.roleAdmin
  },
  {
    state: GlobalConstants.orderPath,
    name: GlobalConstants.sidebarManageOrder,
    type: 'link',
    icon: GlobalConstants.orderIcon,
    role: GlobalConstants.roleAny
  },
  {
    state: GlobalConstants.billPath,
    name: GlobalConstants.sidebarViewBill,
    type: 'link',
    icon: GlobalConstants.billIcon,
    role: GlobalConstants.roleAny
  }
];

@Injectable()
export class MenuItems {

  getMenuItem(): Menu[] {
    return MENUITEMS;
  }
}



