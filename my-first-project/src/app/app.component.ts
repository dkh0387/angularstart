import {Component, OnInit} from '@angular/core';
import {FriendsAddingServiceService} from "./service/friends-adding-service.service";

/*
Web component:
consists of a .html template, .css style and this .ts file (JS)
 */
const imageAlts = ['hamster1', 'hamster2', 'hamster3'];

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    title = 'my-first-project';

    friendsAddingService: FriendsAddingServiceService

    /*
    Example of dependency injection (DI).
     */
    constructor(public newFriendsAddingService: FriendsAddingServiceService) {
        this.friendsAddingService = newFriendsAddingService;
    }

    ngOnInit(): void {
    }
}
