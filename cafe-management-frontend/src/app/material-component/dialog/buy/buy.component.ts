import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SnackbarService} from "../../../services/snackbar.service";
import {BuyService} from "../../../services/buy.service";
import {GlobalConstants} from "../../../shared/global-constants";
import {TranslateService} from "@ngx-translate/core";
import {PayPalService} from "../../../services/paypal.service";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

  buyForm: any = FormGroup;
  payPalURL: string = GlobalConstants.payPalURL + GlobalConstants.payPalAuthClientID;
  @ViewChild("paymentRef", {static: true}) paymentRef!: ElementRef;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private translateService: TranslateService,
              private buyService: BuyService,
              public dialogRef: MatDialogRef<BuyComponent>,
              private snackbarService: SnackbarService,
              private payPalService: PayPalService) {
  }

  ngOnInit(): void {
    this.buyForm = this.formBuilder.group({
      bestseller: [null, [Validators.required]]
    });
    this.buyForm.controls["bestseller"].setValue(this.dialogData.bestseller);
    window.paypal.Buttons().render(this.paymentRef.nativeElement);
  }

  handleBuy() {
    if (this.translateService.currentLang === GlobalConstants.RUS) {
      window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
    } else if (this.translateService.currentLang === GlobalConstants.GER) {
      window.open("https://www.paypal.com/de/home", "_blank");
    }
  }

}
