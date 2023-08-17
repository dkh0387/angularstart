import {Component} from '@angular/core';

/*
Web component:
consists of a .html template, .css style and this .ts file (JS)
NOTE: HelloworldComponent has to be imported in helloworld.module.ts!
 */
@Component({
  selector: 'hello-world',
  templateUrl: './helloworld.component.html',
  styleUrls: ['./helloworld.component.css']
})
/*
Within this class all JS code.
 */
export class HelloworldComponent {
  title = 'my-first-project';

  onClick() {
    alert("Hello World!")
  }
}
