import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SnackbarService} from "../../../services/snackbar.service";
import {BuyService} from "../../../services/buy.service";
import {GlobalConstants} from "../../../shared/global-constants";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.scss']
})
export class BuyComponent implements OnInit {

  buyForm: any = FormGroup;
  service: string | undefined;
  // TODO: trigger on change language settings...
  buyServiceDialogTitle: string = GlobalConstants.buyServiceDialogTitleRUS;
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
    this.service = this.dialogData.service;
    this.buyForm.controls["service"].setValue(this.service);
  }

  handleBuy() {
    // TODO: redirect to PayPal, Tinkoff etc. (depending on language settings)
    window.open("https://www.tinkoff.ru/rm/khaskina.elena2/uYwTe8791/", "_blank");
  }
}
