import {Injectable} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {GlobalConstants} from "../shared/global-constants";
import {Documents} from "../bo/documents";

@Injectable({
    providedIn: 'root'
})
export class DocumentsService {

    constructor(private translateService: TranslateService) {
    }

    getAll() {
        var document1 = new Documents(GlobalConstants.document1, GlobalConstants.document1Url);
        this.setTranslatedDescriptionFor(GlobalConstants.document1DescriptionTranslateKey, document1);
        var document2 = new Documents(GlobalConstants.document2, GlobalConstants.document2Url);
        this.setTranslatedDescriptionFor(GlobalConstants.document2DescriptionTranslateKey, document2);
        var document3 = new Documents(GlobalConstants.document3, GlobalConstants.document3Url);
        this.setTranslatedDescriptionFor(GlobalConstants.document3DescriptionTranslateKey, document3);
        return [document1, document2, document3];
    }

    private setTranslatedDescriptionFor(descrKey: string, document: Documents) {
        return this.translateService.get(descrKey).subscribe((res: string) => {
            document.setDescription(res);
        });
    }
}
