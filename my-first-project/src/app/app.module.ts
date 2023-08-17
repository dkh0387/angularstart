import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {HamsterCardComponent} from './hamster-card/hamster-card.component';
import {ProposalsComponent} from './proposals/proposals.component';
import {ProfileRowComponent} from './profile-row/profile-row.component';
import {FriendBoxComponent} from './friend-box/friend-box.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
/*
Example of usage Icon API from angular material lib.
*/
import {MatIconModule} from '@angular/material/icon';


@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        HamsterCardComponent,
        ProposalsComponent,
        ProfileRowComponent,
        FriendBoxComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatIconModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
