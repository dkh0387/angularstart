import {Component, OnInit} from '@angular/core';
import {PayPalService} from "../services/paypal.service";

@Component({
    selector: 'app-confirm-payment',
    templateUrl: './confirm-payment.component.html',
    styleUrls: ['./confirm-payment.component.scss']
})
export class ConfirmPaymentComponent implements OnInit {

    transactionId = "";

    constructor(private payPalService: PayPalService) {
    }

    ngOnInit(): void {
    }

}
