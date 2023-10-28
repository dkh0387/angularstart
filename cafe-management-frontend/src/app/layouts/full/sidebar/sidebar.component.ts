import {ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import {MediaMatcher} from '@angular/cdk/layout';
import {MenuItems} from "../../../shared/menu-items";
import {RouteGuardService} from "../../../services/route-guard.service";
import {GlobalConstants} from "../../../shared/global-constants";

/**
 * Sidebar menu.
 * Here we render different sidebar menu items according to MenuItems.
 */
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: []
})
export class AppSidebarComponent implements OnDestroy {
  mobileQuery: MediaQueryList;
  userRole: any;
  tokenPalyload: any;
  homePath = '/' + GlobalConstants.homePath + '/';
  roleAny = GlobalConstants.roleAny;

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private routeGuardService: RouteGuardService,
    public menuItems: MenuItems
  ) {
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
    this.tokenPalyload = this.routeGuardService.decodeToken();
    this.userRole = this.tokenPalyload.role;
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  protected readonly GlobalConstants = GlobalConstants;
}
