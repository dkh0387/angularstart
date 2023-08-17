import {Component, Input, OnInit} from '@angular/core';
import {FriendsAddingServiceService} from "../service/friends-adding-service.service";

@Component({
    selector: 'app-proposals',
    templateUrl: './proposals.component.html',
    styleUrls: ['./proposals.component.scss']
})
export class ProposalsComponent implements OnInit {

    @Input() imgPath: string = '';
    @Input() imgAlt: string = '';
    @Input() name: string = '';
    @Input() description: string = '';

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
