import {GlobalConstants} from "../shared/global-constants";
import {SnackbarService} from "../services/snackbar.service";

export class ResponseHadler {

  responseMessage: any;

  buildResponseMessageFromError(error: any) {
    this.responseMessage = (error.error?.message == null) ? (GlobalConstants.error) : error.error?.message;
  }

  protected logAndShowError(error: any, snackBarService: SnackbarService) {
    console.log(error);
    this.buildResponseMessageFromError(error);
    snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
  }

}
