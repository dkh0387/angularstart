import {Component, OnInit} from '@angular/core';
import {GlobalConstants} from "../shared/global-constants";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-about-me',
  templateUrl: './about-me.component.html',
  styleUrls: ['./about-me.component.scss']
})
export class AboutMeComponent implements OnInit {
  aboutMeTitle: string = GlobalConstants.aboutMePageTitleRUS;

  constructor() {
  }

  ngOnInit(): void {
  }

  protected readonly FormGroup = FormGroup;
}
