import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {GlobalConstants} from "../../../shared/global-constants";
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";
import {PayPalService} from "../../../services/paypal.service";

@Component({
    selector: 'app-buy',
    templateUrl: './buy.component.html',
    styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

    documentName: any;
    documentDescription: any;
    documentPrice: any;

    @ViewChild("paymentRef", {static: true}) paymentRef!: ElementRef;

    constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
                private translateService: TranslateService,
                public dialogRef: MatDialogRef<BuyComponent>,
                private router: Router,
                private payPalService: PayPalService) {
    }

    ngOnInit(): void {
        this.documentName = this.dialogData.documentName;
        this.documentDescription = this.dialogData.documentDescription;
        this.documentPrice = this.dialogData.documentPrice;
        window.paypal.Buttons(
            {
                style: {
                    layout: "horizontal",
                    color: "blue",
                    shape: "rect",
                    label: "paypal"
                },
                createOrder: (data: any, actions: any) => {
                    return actions.order.create({
                        purchase_units: [{amount: {value: this.dialogData.documentPrice.toString(), currency_code: GlobalConstants.currencyEUR}}]
                    });
                },
                onApprove: (data: any, actions: any) => {
                    return actions.order.capture().then((details: any) => {
                        if (details.status === GlobalConstants.paymentStatusCodeCompleted) {
                            this.payPalService.transactionId = details.id;
                            // TODO: path the details.id into the url
                            this.router.navigate([this.router.navigate(["/" + GlobalConstants.paypalConfirmationPath])
                                .catch((error) => console.log(error))
                            ]);
                            ; //TODO: create a thank you for your payment page
                            //Send email with google drive link to the Document
                        }
                    });
                },
                onError: (error: any) => {
                    console.log(error);
                }
            }
        ).render(this.paymentRef.nativeElement);
    }

    handleBuy() {
        if (this.translateService.currentLang === GlobalConstants.RUS) {
            window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
        }
    }

}
