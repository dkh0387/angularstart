import {Component, OnInit} from '@angular/core';
import {PayPalService} from "../services/paypal.service";

@Component({
  selector: 'app-confirm-payment',
  templateUrl: './confirm-payment.component.html',
  styleUrls: ['./confirm-payment.component.scss']
})
export class ConfirmPaymentComponent implements OnInit {

  currentTransactionId: any;

  constructor(private payPalService: PayPalService) {
    this.currentTransactionId = this.payPalService.transactionId;
  }

  ngOnInit(): void {
  }

  get transactionId(): string {
    return this.payPalService.transactionId;
  }

  set transactionId(value: string) {
    this.payPalService.transactionId = value;
    this.currentTransactionId = value;
  }

}
