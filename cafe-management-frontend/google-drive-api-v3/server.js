/*
 * Express server running on port 5001 to communicate with the Google Drive API.
 */
const express = require("express");
const app = express();
const PORT = 5001;

/*
 * Test the server is running.
 */

/*
* Here we are importing fs to access the local file system of node.js server.
* The readline module provides an interface for reading data from a Readable stream (such as process.stdin) one line at a time.
* Googleapis is a node.js client library for using Google APIs. Support for authorization and authentication with OAuth 2.0, API Keys,
* and JWT tokens is included.
* */
const fs = require("fs");
const readline = require("readline");
const {google} = require("googleapis");

/* If modifying these scopes, delete token.json.
 * The SCOPES variable contains what type of permission we are getting for Google Drive. For example, if we want read-only permission  * for metadata, then we should add "drive.metadata.readonly*" at the end of the link.
 */
const SCOPES = ["https://www.googleapis.com/auth/drive"];

/* The file token.json stores the user"s access and refresh tokens, and is
* created automatically when the authorization flow completes for the first time.
*/
const TOKEN_PATH = "token.json";

let auth;

/*
 * Read the file and call the authorize function to get the authorization from Google.
 */
fs.readFile("credentials.json", (err, content) => {
  if (err) return console.log("Error loading client secret file:", err);
  // Authorize a client with credentials, then call the Google Drive API.
  authorize(JSON.parse(content));
});

/**
 * Create an OAuth2 client with the given credentials, and then execute the
 * given callback function.
 * @param {Object} credentials The authorization client credentials.
 * @param {function} callback The callback to call with the authorized client.
 */
function authorize(credentials) {
  const {client_secret, client_id, redirect_uris} = credentials.web;
  const oAuth2Client = new google.auth.OAuth2(
    client_id,
    client_secret,
    redirect_uris[0]
  );

  // Check if we have previously stored a token.
  fs.readFile(TOKEN_PATH, (err, token) => {
    if (err) return getAccessToken(oAuth2Client);
    oAuth2Client.setCredentials(JSON.parse(token));
    auth = oAuth2Client;
  });
}

/**
 * Get and store new token after prompting for user authorization, and then
 * execute the given callback with the authorized OAuth2 client.
 * @param {google.auth.OAuth2} oAuth2Client The OAuth2 client to get token for.
 * @param {getEventsCallback} callback The callback for the authorized client.
 */

function getAccessToken(oAuth2Client) {
  const authUrl = oAuth2Client.generateAuthUrl({
    access_type: "offline",
    scope: SCOPES,
  });
  console.log("Authorize this app by visiting this url:", authUrl);
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  rl.question("Enter the code from that page here: ", (code) => {
    rl.close();
    oAuth2Client.getToken(code, (err, token) => {
      if (err) return console.error("Error retrieving access token", err);
      oAuth2Client.setCredentials(token);
      // Store the token to disk for later program executions
      fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
        if (err) return console.error(err);
        console.log("Token stored to", TOKEN_PATH);
      });
      auth = authoAuth2Client;
    });
  });
}

