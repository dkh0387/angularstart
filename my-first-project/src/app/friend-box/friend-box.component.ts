import {Component, Input, OnInit} from '@angular/core';
import {FriendsAddingServiceService} from "../service/friends-adding-service.service";

@Component({
    selector: 'app-friend-box',
    templateUrl: './friend-box.component.html',
    styleUrls: ['./friend-box.component.scss']
})
export class FriendBoxComponent implements OnInit {

    @Input() name: string = '';
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
