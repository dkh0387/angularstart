import {TestBed} from '@angular/core/testing';

import {PayPalService} from './paypal.service';
import {environment} from "../../environments/environment";
import {GlobalConstants} from "../shared/global-constants";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('PaypalService', () => {
  let httpTestingController: HttpTestingController;
  let service: PayPalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(PayPalService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('#getDetails should return expected dashboard data', (done) => {
    const expectedData = {
      "scope": "https://uri.paypal.com/services/checkout/one-click-with-merchant-issued-token https://uri.paypal.com/services/invoicing https://uri.paypal.com/services/vault/payment-tokens/read https://uri.paypal.com/services/disputes/read-buyer https://uri.paypal.com/services/payments/realtimepayment https://uri.paypal.com/services/disputes/update-seller https://uri.paypal.com/services/payments/payment/authcapture openid https://uri.paypal.com/services/disputes/read-seller Braintree:Vault https://uri.paypal.com/services/payments/refund https://api.paypal.com/v1/vault/credit-card https://uri.paypal.com/services/pricing/quote-exchange-rates/read https://uri.paypal.com/services/billing-agreements https://api.paypal.com/v1/payments/.* https://uri.paypal.com/payments/payouts https://uri.paypal.com/services/vault/payment-tokens/readwrite https://api.paypal.com/v1/vault/credit-card/.* https://uri.paypal.com/services/shipping/trackers/readwrite https://uri.paypal.com/services/subscriptions https://uri.paypal.com/services/applications/webhooks",
      "access_token": "A21AAJy7QoijNvmnW6k6BhIGz5bG6c_0svFfthw87gXN349c6zwj-BspGeERQcOEfKZkOXjoMS9XrDA_IR6g1lrhwN2DrvKqg",
      "token_type": "Bearer",
      "app_id": "APP-80W284485P519543T",
      "expires_in": 32400,
      "nonce": "2023-11-25T11:38:53Zl2VD64suULviSVW6oXQXGj2JQd00jPF5jWmlWrtnPQU"

    };

    service.postForAccessToken().subscribe(data => {
      expect(data).toEqual(expectedData);
      done();
    });

    const testRequest = httpTestingController.expectOne(GlobalConstants.payPalAuthTokenAPI);

    testRequest.flush(expectedData);
  });

  it('#getDetails should use GET to retrieve data', () => {
    service.postForAccessToken().subscribe();

    const testRequest = httpTestingController.expectOne(GlobalConstants.payPalAuthTokenAPI);

    expect(testRequest.request.method).toEqual("POST");
  });
});
