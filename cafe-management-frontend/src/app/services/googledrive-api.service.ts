import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import credentialsSource from "../../../google-drive-api-v3/credentials.json";
import tokenSource from "../../../google-drive-api-v3/token.json";
import apiKeySource from "../../../google-drive-api-v3/apikey.json";


@Injectable({
  providedIn: "root"
})
export class GoogledriveApiService {

  //TODO: make it a document attribute.
  fileId: string = "1QG9kGwFI4tYW4UHgSNBCQQw957SfIanL"
  scope = "https://www.googleapis.com/auth/drive"
  apiKey = ""
  access_token = ""

  constructor(private httpClient: HttpClient) {
    this.loadCredentials();
  }

  loadCredentials() {
    try {
      this.refreshToken();
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

  private refreshToken() {
    this.httpClient.post("https://www.googleapis.com/oauth2/v4/token", {
      client_id: credentialsSource.web.client_id,
      client_secret: credentialsSource.web.client_secret,
      refresh_token: tokenSource.access_token,
      grant_type: "refresh_token"
    }).subscribe((res: any) => this.access_token = res.access_token);
  }
}
