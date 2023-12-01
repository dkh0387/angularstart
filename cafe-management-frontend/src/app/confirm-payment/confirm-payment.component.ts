import {Component, OnInit} from '@angular/core';
import {PayPalService} from "../services/paypal.service";
import {GlobalConstants} from "../shared/global-constants";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-confirm-payment',
  templateUrl: './confirm-payment.component.html',
  styleUrls: ['./confirm-payment.component.scss']
})
export class ConfirmPaymentComponent implements OnInit {

  currentTransactionId: any;
  downloadBillIcon: string = GlobalConstants.downloadBillIcon;
  mainPageIcon: string = GlobalConstants.mainPageIcon;

  displayedColumns: string[] = ["transactionId", "dokumentName", "price"];
  orderForm: any = FormGroup;

  constructor(private payPalService: PayPalService, private formBuilder: FormBuilder, private router: Router) {
    this.currentTransactionId = this.payPalService.transactionId;
  }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      transactionId: [null, [Validators.required]],
      dokumentName: [null, [Validators.required]],
      price: [null, [Validators.pattern(GlobalConstants.priceRegex)]]
    });
    this.orderForm.controls["transactionId"].setValue(this.currentTransactionId);
    //TODO: provide the Documents object for setting here...
  }

  get transactionId(): string {
    return this.payPalService.transactionId;
  }

  set transactionId(value: string) {
    this.payPalService.transactionId = value;
    this.currentTransactionId = value;
  }

  protected readonly FormGroup = FormGroup;

  handleDownloadBillAction() {
    console.log("handleDownloadBillAction")
  }

  handleGoToHomepageAction() {
    this.router.navigate(["/"]);
  }
}
