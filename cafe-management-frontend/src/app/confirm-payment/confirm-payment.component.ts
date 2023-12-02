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
  currentDocumentName: any;
  currentDocumentPrice: any;
  downloadBillIcon: string = GlobalConstants.downloadBillIcon;
  mainPageIcon: string = GlobalConstants.mainPageIcon;

  displayedColumns: string[] = ["transactionId", "dokumentName", "price"];
  orderForm: any = FormGroup;

  constructor(private payPalService: PayPalService, private formBuilder: FormBuilder, private router: Router) {
    this.currentTransactionId = this.payPalService.transactionId;
    this.currentDocumentName = this.payPalService.documentName;
    this.currentDocumentPrice = this.payPalService.documentPrice;

  }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      transactionId: [null, [Validators.required]],
      documentName: [null, [Validators.required]],
      documentPrice: [null, [Validators.required]]
    });
    this.orderForm.controls["transactionId"].setValue(this.currentTransactionId);
    this.orderForm.controls["documentName"].setValue(this.currentDocumentName);
    this.orderForm.controls["documentPrice"].setValue(this.currentDocumentPrice);
  }

  protected readonly FormGroup = FormGroup;

  handleDownloadBillAction() {
    console.log("handleDownloadBillAction")
  }

  get transactionId(): string {
    return this.payPalService.transactionId;
  }

  set transactionId(value: string) {
    this.payPalService.transactionId = value;
    this.currentTransactionId = value;
  }

  get documentName(): string {
    return this.currentDocumentName;
  }

  set documentName(value: string) {
    this.payPalService.documentName = value;
    this.currentDocumentName = value;
  }

  get documentPrice(): string {
    return this.currentDocumentPrice;
  }

  set documentPrice(value: string) {
    this.payPalService.documentPrice = value;
    this.currentDocumentPrice = value;
  }

  handleGoToHomepageAction() {
    this.router.navigate(["/"]);
  }
}
