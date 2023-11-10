import {Routes} from '@angular/router';
import {ManageCategoryComponent} from "./manage-category/manage-category.component";
import {RouteGuardService} from "../services/route-guard.service";
import {GlobalConstants} from "../shared/global-constants";
import {ManageProductComponent} from "./manage-product/manage-product.component";
import {ManageOrderComponent} from "./manage-order/manage-order.component";


export const MaterialRoutes: Routes = [
  {
    path: GlobalConstants.categoryPath,
    component: ManageCategoryComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin]}
  },
  {
    path: GlobalConstants.productPath,
    component: ManageProductComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin]}
  },
  {
    path: GlobalConstants.orderPath,
    component: ManageOrderComponent,
    canActivate: [RouteGuardService],
    data: {expectedRole: [GlobalConstants.roleAdmin, GlobalConstants.roleUser]}
  }
];
