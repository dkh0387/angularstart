import {GlobalConstants} from "../shared/global-constants";

export class ResponseHadler {

  responseMessage: any;

  buildResponseMessageFrom(error: any) {
    this.responseMessage = (error.error?.message == null) ? (GlobalConstants.error) : error.error?.message;
  }

}
