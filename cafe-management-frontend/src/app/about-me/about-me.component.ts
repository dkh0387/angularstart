import {Component, OnInit} from '@angular/core';
import {GlobalConstants} from "../shared/global-constants";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-about-me',
  templateUrl: './about-me.component.html',
  styleUrls: ['./about-me.component.scss']
})
export class AboutMeComponent implements OnInit {
  aboutMeTitle: string = GlobalConstants.aboutMePageTitleRUS;
  aboutMeContentTitle: string = GlobalConstants.aboutMeContentTitleRUS;
  language: string | null = GlobalConstants.RUS;

  constructor(private route: ActivatedRoute) {
    this.language = this.route.snapshot.paramMap.get("language");
  }

  ngOnInit(): void {
    this.changeLanguage();
    //history.pushState({}, "language", `?language=${this.language}`);
  }

  goBack() {
    //history.back();
  }

  private changeLanguage() {
    if (this.language === GlobalConstants.RUS) {
      this.aboutMeTitle = GlobalConstants.aboutMePageTitleRUS;
      this.aboutMeContentTitle = GlobalConstants.aboutMeContentTitleRUS;
    } else if (this.language === GlobalConstants.GER) {
      this.aboutMeTitle = GlobalConstants.aboutMePageTitleGER;
      this.aboutMeContentTitle = GlobalConstants.aboutMeContentTitleGER;
    }
  }

}
