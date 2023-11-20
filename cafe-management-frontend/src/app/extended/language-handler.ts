import {GlobalConstants} from "../shared/global-constants";

export class LanguageHandler {

  languageSettings = GlobalConstants.RUS;
  rus = GlobalConstants.RUS;
  ger = GlobalConstants.GER;
  /*
 * Language Settings
 */
  headerIconChangeLanguage = GlobalConstants.headerIconChangeLanguage;
  mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextRUS;
  mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextRUS;
  mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextRUS;

  mainPageMenuChangeLanguageGERTitle = GlobalConstants.mainPageMenuChangeLanguageGERTitle;
  mainPageMenuChangeLanguageRUSTitle = GlobalConstants.mainPageMenuChangeLanguageRUSTitle;

  // Best sellers
  servicePageTitle = GlobalConstants.servicePageTitleRUS;
  servicePageService1 = GlobalConstants.servicePageService1RUS;
  servicePageService2 = GlobalConstants.servicePageService2RUS;
  servicePageService3 = GlobalConstants.servicePageService3RUS;
  servicePageService4 = GlobalConstants.servicePageService4RUS;
  servicePageService5 = GlobalConstants.servicePageService5RUS;
  servicePageService6 = GlobalConstants.servicePageService6RUS;
  servicePageService7 = GlobalConstants.servicePageService7RUS;


  constructor(languageSettings: string) {
    this.languageSettings = languageSettings;
  }

  changeMainPageLanguageTo(language: string) {
    if (language === GlobalConstants.GER) {
      this.languageSettings = GlobalConstants.GER
      this.mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextGER;
      this.mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextGER;
      this.mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextGER;
      this.servicePageTitle = GlobalConstants.servicePageTitleGER;
      this.servicePageService1 = GlobalConstants.servicePageService1GER;
      this.servicePageService2 = GlobalConstants.servicePageService2GER;
      this.servicePageService3 = GlobalConstants.servicePageService3GER;
      this.servicePageService4 = GlobalConstants.servicePageService4GER;
      this.servicePageService5 = GlobalConstants.servicePageService5GER;
      this.servicePageService6 = GlobalConstants.servicePageService6GER;
      this.servicePageService7 = GlobalConstants.servicePageService7GER;
    } else if (language === GlobalConstants.RUS) {
      this.languageSettings = GlobalConstants.RUS
      this.mainPageMenuAboutTextActual = GlobalConstants.mainPageMenuAboutTextRUS;
      this.mainPageMenuProjectsTextActual = GlobalConstants.mainPageMenuProjectsTextRUS;
      this.mainPageMenuContactTextActual = GlobalConstants.mainPageMenuContactTextRUS;
      this.servicePageTitle = GlobalConstants.servicePageTitleRUS;
      this.servicePageService1 = GlobalConstants.servicePageService1RUS;
      this.servicePageService2 = GlobalConstants.servicePageService2RUS;
      this.servicePageService3 = GlobalConstants.servicePageService3RUS;
      this.servicePageService4 = GlobalConstants.servicePageService4RUS;
      this.servicePageService5 = GlobalConstants.servicePageService5RUS;
      this.servicePageService6 = GlobalConstants.servicePageService6RUS;
      this.servicePageService7 = GlobalConstants.servicePageService7RUS;
    }
  }

}
