import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class InputOutput {
    public static final String USERNAME = "Redacted.apps.googleusercontent.com";
    public static final String PASSWORD = "Kinh";
    public static final String SHEETURL = "https://docs.google.com/spreadsheets/d/1zo8kQKPG4OW_od4jRdfTl9BG3pyd9GTwRIzkm36tajw/edit#gid=0";

    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/spreadsheets");

    public static void read() throws IOException, URISyntaxException {
        Credential credential = getCredentials();
        System.out.println(credential.toString());
    }

    public static Credential getCredentials() throws IOException, URISyntaxException {
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        String authorizationUrl =
                new GoogleAuthorizationCodeRequestUrl(USERNAME, SHEETURL, SCOPES).build();

        Desktop.getDesktop().browse(new URL(authorizationUrl).toURI());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String code = in.readLine(); //NO CLUE WHAT TO PUT HERE

        GoogleTokenResponse response =
                new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, USERNAME, PASSWORD,
                        code, SHEETURL).execute();

        return new GoogleCredential.Builder().setClientSecrets(USERNAME, PASSWORD)
                .setJsonFactory(jsonFactory).setTransport(transport).build()
                .setAccessToken(response.getAccessToken()).setRefreshToken(response.getRefreshToken());
        //What do I do after I receive the credentials?
    }
}
