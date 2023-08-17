import {Component} from '@angular/core';

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
export class AppComponent {

  private readonly imgHamsterPath = 'assets/img/hamster/';
  private readonly ingFileType = '.jpg';
  title = 'my-first-project';

  names = ['Frederik', 'Mark', 'Denis']

  descriptions = ['3 Jahre alt', 'esse gerne Käse', 'bin sportlich']

  postTexts = ['Hallo! Mein Name ist Frederik, ich bin hier, um neue Freunde zu treffen!',
    'Hallo! Mein Name ist Marc, ich bin hier, um neue Freunde zu treffen!',
    'Hallo! Mein Name ist Denis, ich bin hier, um neue Freunde zu treffen!'
  ]

  images = imageAlts.map(value => this.imgHamsterPath + value + this.ingFileType)

  protected readonly imageAlts = imageAlts;
}
