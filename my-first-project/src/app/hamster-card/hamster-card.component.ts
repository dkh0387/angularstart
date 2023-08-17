import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-hamster-card',
  templateUrl: './hamster-card.component.html',
  styleUrls: ['./hamster-card.component.scss']
})
export class HamsterCardComponent implements OnInit {
  /*
  Input parameter:
  we reuse the hamster-card component with different image and text values.
   */
  @Input() text: string = '';
  @Input() imgPath: string = '';
  @Input() imgAlt: string = '';

  constructor() {
  }

  ngOnInit(): void {
  }

}
