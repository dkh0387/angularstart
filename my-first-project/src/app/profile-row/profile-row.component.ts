import {Component, Input, OnInit} from '@angular/core';
import {FriendsAddingServiceService} from "../service/friends-adding-service.service";

@Component({
    selector: 'app-profile-row',
    templateUrl: './profile-row.component.html',
    styleUrls: ['./profile-row.component.scss']
})
export class ProfileRowComponent implements OnInit {
    @Input() imgPath: string = '';
    @Input() imgAlt: string = '';
    @Input() name: string = '';
    @Input() description: string = '';
    @Input() followLink: string = '';

    friendsAddingService: FriendsAddingServiceService

    /*
    Example of dependency injection (DI). We explicitly bind this one on a `Folgen` link in HTML file.
     */
    constructor(public newFriendsAddingService: FriendsAddingServiceService) {
        this.friendsAddingService = newFriendsAddingService;
    }

    ngOnInit(): void {
    }

}
