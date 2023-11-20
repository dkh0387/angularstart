import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SnackbarService} from "../../../services/snackbar.service";
import {BuyService} from "../../../services/buy.service";
import {GlobalConstants} from "../../../shared/global-constants";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

  buyForm: any = FormGroup;
  service: string | undefined;
  buyServiceDialogClose: string = GlobalConstants.buyServiceDialogCloseRUS;
  buyServiceDialogSubmit: string = GlobalConstants.buyServiceDialogSubmitRUS;
  buyServiceDialogServiceLabel: string = GlobalConstants.buyServiceDialogServiceLabelRUS;

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
              private formBuilder: FormBuilder,
              private buyService: BuyService,
              public dialogRef: MatDialogRef<BuyComponent>,
              private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.buyForm = this.formBuilder.group({
      service: [null, [Validators.required]]
    });
    this.changeLanguage();
    this.service = this.dialogData.service;
    this.buyForm.controls["service"].setValue(this.service);
  }

  private changeLanguage() {
    if (this.dialogData.language === GlobalConstants.RUS) {
      this.buyServiceDialogServiceLabel = GlobalConstants.buyServiceDialogServiceLabelRUS;
      this.buyServiceDialogClose = GlobalConstants.buyServiceDialogCloseRUS;
      this.buyServiceDialogSubmit = GlobalConstants.buyServiceDialogSubmitRUS;
    } else if (this.dialogData.language === GlobalConstants.GER) {
      this.buyServiceDialogServiceLabel = GlobalConstants.buyServiceDialogServiceLabelGER;
      this.buyServiceDialogClose = GlobalConstants.buyServiceDialogClosGER;
      this.buyServiceDialogSubmit = GlobalConstants.buyServiceDialogSubmitGER;
    }
  }

  handleBuy() {
    if (this.dialogData.language === GlobalConstants.RUS) {
      window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
    } else if (this.dialogData.language === GlobalConstants.GER) {
      window.open("https://www.paypal.com/de/home", "_blank");
    }
  }

}
