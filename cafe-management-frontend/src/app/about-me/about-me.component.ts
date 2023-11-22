import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-about-me',
  templateUrl: './about-me.component.html',
  styleUrls: ['./about-me.component.scss']
})
export class AboutMeComponent implements OnInit {

  constructor(private route: ActivatedRoute, private translateService: TranslateService) {

  }

  ngOnInit(): void {

  }

  handleShowMore() {
    //TODO...
  }
}
