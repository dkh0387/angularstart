import {Component, Input} from '@angular/core';

const imageAlts = ['hamster1', 'hamster2', 'hamster3'];

@Component({
  selector: 'app-proposals',
  templateUrl: './proposals.component.html',
  styleUrls: ['./proposals.component.scss']
})
export class ProposalsComponent {
  private readonly imgHamsterPath = 'assets/img/hamster/';
  private readonly ingFileType = '.jpg';

  @Input() imgPath: string = '';
  @Input() imgAlt: string = '';
  @Input() name: string = '';
  @Input() description: string = '';

  names = ['Frederik', 'Mark', 'Denis']

  descriptions = ['3 Jahre alt', 'esse gerne Käse', 'bin sportlich']

  postTexts = ['Hallo! Mein Name ist Frederik, ich bin hier, um neue Freunde zu treffen!',
    'Hallo! Mein Name ist Marc, ich bin hier, um neue Freunde zu treffen!',
    'Hallo! Mein Name ist Denis, ich bin hier, um neue Freunde zu treffen!'
  ]

  images = imageAlts.map(value => this.imgHamsterPath + value + this.ingFileType)

  protected readonly imageAlts = imageAlts;

}
