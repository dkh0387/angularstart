import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-profile-row',
  templateUrl: './profile-row.component.html',
  styleUrls: ['./profile-row.component.scss']
})
export class ProfileRowComponent {
  @Input() imgPath: string = '';
  @Input() imgAlt: string = '';
  @Input() name: string = '';
  @Input() description: string = '';

}
