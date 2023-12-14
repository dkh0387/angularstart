import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import tokenSource from "../../../google-drive-api-v3/token.json";
import apiKeySource from "../../../google-drive-api-v3/apikey.json";


@Injectable({
  providedIn: "root"
})
export class GoogledriveApiService {

  fileId: string = "1QG9kGwFI4tYW4UHgSNBCQQw957SfIanL"
  scope = "https://www.googleapis.com/auth/drive"
  apiKey = ""
  access_token = ""

  constructor(private httpClient: HttpClient) {
    this.readCredentials();
  }

  readCredentials() {
    try {
      this.access_token = tokenSource.access_token;
      this.apiKey = apiKeySource.key;
    } catch (error) {
      console.log(error);
    }
  }

  downloadPDFFile() {
    const headers = {
      "Authorization": `Bearer ${this.access_token}`,
      "Content-type": "application/json",
      "Accept": "application/pdf"
    }
    return this.httpClient
      .get(`https://www.googleapis.com/drive/v3/files/${this.fileId}?key=${this.apiKey}&alt=media`, {
        headers,
        responseType: "blob"
      })
  }
}
