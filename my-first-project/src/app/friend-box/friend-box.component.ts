import {Component, Input} from '@angular/core';

const imageAlts = ['hamster1', 'hamster2', 'hamster3'];

@Component({
  selector: 'app-friend-box',
  templateUrl: './friend-box.component.html',
  styleUrls: ['./friend-box.component.scss']
})
export class FriendBoxComponent {

  private readonly imgHamsterPath = 'assets/img/hamster/';
  private readonly ingFileType = '.jpg';

  @Input() name: string = '';

  names = ['Frederik', 'Mark', 'Denis']
  images = imageAlts.map(value => this.imgHamsterPath + value + this.ingFileType)

  protected readonly imageAlts = imageAlts;

}
