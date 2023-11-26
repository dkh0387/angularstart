import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {GlobalConstants} from "../../../shared/global-constants";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-buy',
    templateUrl: './buy.component.html',
    styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

    documentName: any;
    documentDescription: any;
    @ViewChild("paymentRef", {static: true}) paymentRef!: ElementRef;

    constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
                private translateService: TranslateService,
                public dialogRef: MatDialogRef<BuyComponent>) {
    }

    ngOnInit(): void {
        this.documentName = this.dialogData.documentName;
        this.documentDescription = this.dialogData.documentDescription;
        window.paypal.Buttons().render(this.paymentRef.nativeElement);
    }

    handleBuy() {
        if (this.translateService.currentLang === GlobalConstants.RUS) {
            window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
        }
    }

}
