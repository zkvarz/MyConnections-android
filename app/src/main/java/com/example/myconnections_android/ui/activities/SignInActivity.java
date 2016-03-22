package com.example.myconnections_android.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.models.FacebookUserResponse;
import com.example.myconnections_android.api.models.LoginFacebook;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import static android.text.TextUtils.isEmpty;
import static com.example.myconnections_android.api.Api.getLoginByFacebookUrl;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button facebookLogin = (Button) findViewById(R.id.facebookLogin);
        facebookLogin.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Logger.debug(getClass(), "onActivityResult requestCode");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebookLogin:
                Logger.debug(getClass(), "Click!");
                // Initialize Facebook SDK
                FacebookSdk.sdkInitialize(getApplicationContext());

                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                Logger.debug(MainActivity.class.getClass(), "onSuccess!");
                                Log.d("HEY", "onSuccess!!!");
                                Log.d("TOKEN!", loginResult.getAccessToken().getToken());
                                Log.d("TOKEN id!", loginResult.getAccessToken().getUserId());

                                //Graph for getting additional info
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(
                                                    JSONObject object,
                                                    GraphResponse response) {
                                                Log.d("MY ID", "onSuccess!!!");
                                                FacebookUserResponse facebookUserResponse = new Gson().fromJson(object.toString(), FacebookUserResponse.class);
                                                Log.d("MY ID", "facebookUserResponse id" + facebookUserResponse.getId());
                                                Log.d("MY ID", "facebookUserResponse name" + facebookUserResponse.getName());
                                                facebookLogin(facebookUserResponse.getId(), loginResult.getAccessToken().getToken());
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,link");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Logger.debug(MainActivity.class.getClass(), "onCancel!");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Logger.debug(getClass(), "Error! " + error.getMessage());
                            }
                        });
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile", "user_friends"));
                break;

        }
    }

    private void facebookLogin(String id, String token) {

      /* new LoginByFacebookRequest(new LoginFacebook(id, token), new ICallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Logger.debug(getClass(), "FACEBOOK LOGIN RESPONSE ");
                Logger.debug(getClass(), "FACEBOOK LOGIN RESPONSE getFacebookId " + loginResponse.getFacebookId());
                Logger.debug(getClass(), "FACEBOOK LOGIN RESPONSE getToken " + loginResponse.getToken());
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "FACEBOOK LOGIN ERROR " + error.getErrorMessage());
            }
        }).execute();*/

        String json = new Gson().toJson(new LoginFacebook(id, token));

        new PostClient().execute(json);


        findViewById(R.id.registerLayout).setVisibility(View.INVISIBLE);
        findViewById(R.id.socialLayout).setVisibility(View.INVISIBLE);
        LinearLayout enterPhoneLayout = (LinearLayout) findViewById(R.id.enterPhoneLayout);
        enterPhoneLayout.setVisibility(View.VISIBLE);
        EditText phone = (EditText) enterPhoneLayout.findViewById(R.id.phone);

       if(!isEmpty(phone.getText())){

       }
        // If user is not registered, ask phone number:
    }

    private StringBuffer request(String urlParameters) {
        Logger.debug(getClass(), "urlParameters! " + urlParameters);

        String urlString = getLoginByFacebookUrl();
        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "application/json");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

            Logger.debug(getClass(), "line! " + line);

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return chaine;
    }

    /*
        * POST EXAMPLE
        */
    // We must do this as a background task, elsewhere our app crashes
    class PostClient extends AsyncTask<String, Void, String> {

        public String doInBackground(String... IO) {

            Logger.debug(getClass(), "urlParameters! " + IO[0]);
            // Predefine variables
            String io = new String(IO[0]);
            URL url;

            try {
                // Stuff variables
                url = new URL("https://myconnections-backend-zkvarz.c9users.io/account/facebookLogin");
                String param =  io;
                Log.d("TAG", "param:" + param);

                // Open a connection using HttpURLConnection
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(param.getBytes().length);
                con.setRequestProperty("Content-Type", "application/json");
                con.connect();

                //Send request
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(IO[0]);
                osw.flush();
                osw.close();

                StringBuffer response = new StringBuffer("");

                String line = null;
                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d("TAG", "!=200: " + in);
                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                Log.d("TAG", "POST request response: " + response);
                Log.d("TAG", "POST request response STATUS: " + con.getResponseCode());
                Log.d("TAG", "POST request response STATUS MESSAGE: " + con.getResponseMessage());

            } catch (Exception e) {
                Log.d("TAG", "Exception " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            // Set null and we´e good to go
            return null;
        }
    }

}
