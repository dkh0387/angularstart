import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SnackbarService} from "../../../services/snackbar.service";
import {BuyService} from "../../../services/buy.service";
import {GlobalConstants} from "../../../shared/global-constants";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

  buyForm: any = FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private translateService: TranslateService,
              private buyService: BuyService,
              public dialogRef: MatDialogRef<BuyComponent>,
              private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.buyForm = this.formBuilder.group({
      bestseller: [null, [Validators.required]]
    });
    this.buyForm.controls["bestseller"].setValue(this.dialogData.bestseller);
  }

  handleBuy() {
    if (this.translateService.currentLang === GlobalConstants.RUS) {
      window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
    } else if (this.translateService.currentLang === GlobalConstants.GER) {
      window.open("https://www.paypal.com/de/home", "_blank");
    }
  }

}
