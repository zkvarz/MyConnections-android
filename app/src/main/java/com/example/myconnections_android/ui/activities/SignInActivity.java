package com.example.myconnections_android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.models.FacebookUserResponse;
import com.example.myconnections_android.api.models.LoginFacebook;
import com.example.myconnections_android.api.models.Session;
import com.example.myconnections_android.api.requests.LoginFacebookRequest;
import com.example.myconnections_android.api.requests.LoginGoogleRequest;
import com.example.myconnections_android.api.requests.LoginTwitterRequest;
import com.example.myconnections_android.api.requests.UpdateUserRequest;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.preferences.AppPreference;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONObject;

import java.util.Arrays;

import static android.text.TextUtils.isEmpty;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private EditText phoneText;
    private GoogleApiClient mGoogleApiClient;
    private TwitterAuthClient client;

    private static LoginResponse loginResponse;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (AppPreference.getInstance().isSessionValid()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        client = new TwitterAuthClient();

        Button facebookLogin = (Button) findViewById(R.id.facebookLogin);
        facebookLogin.setOnClickListener(this);
        Button googleLogin = (Button) findViewById(R.id.googleLogin);
        googleLogin.setOnClickListener(this);
        Button twitterLogin = (Button) findViewById(R.id.twitterLogin);
        twitterLogin.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        Logger.debug(getClass(), "onActivityResult requestCode " + requestCode);
        Logger.debug(getClass(), "onActivityResult resultCode " + resultCode);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else {
            client.onActivityResult(requestCode, resultCode, data);
        }

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
                                                facebookServerLogin(facebookUserResponse.getId(), loginResult.getAccessToken().getToken());
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
            case R.id.googleLogin:
                googleSignIn();
                break;
            case R.id.sendPhoneButton:
                Logger.debug(getClass(), "sendPhoneButton");
                if (!isEmpty(phoneText.getText())) {
                    updateProfile();
                }
                break;
            case R.id.twitterLogin:
                authorizeTwitter();
                break;
        }
    }

    public void authorizeTwitter() {
        client.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(final Result<TwitterSession> twitterSessionResult) {
                String output = "Status: " +
                        "Your login was successful " +
                        twitterSessionResult.data.getUserName() +
                        "\nAuth Token Received: " +
                        twitterSessionResult.data.getAuthToken().token +
                        "\nUserId: " +
                        twitterSessionResult.data.getUserId();

                TwitterAuthToken authToken = twitterSessionResult.data.getAuthToken();

                Log.e("output", output);
                Logger.debug(SignInActivity.class.getClass(), "twitter token " + authToken.token);
                Logger.debug(SignInActivity.class.getClass(), "twitter secret " + authToken.secret);
                twitterServerLogin(authToken.token, authToken.secret);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Logger.debug(getClass(), "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Logger.debug(getClass(), "acct.getDisplayName() " + acct.getDisplayName());
            Logger.debug(getClass(), "acct.getId() " + acct.getId());
            Logger.debug(getClass(), "acct.getIdToken() " + acct.getIdToken());
            googleServerSignIn(acct.getIdToken());
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "Authorization error!", Toast.LENGTH_LONG).show();
        }
    }

    private void googleServerSignIn(final String idToken) {
        new LoginGoogleRequest(new Session(idToken, null), new ICallback<LoginResponse>() {

            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Logger.debug(getClass(), "GOOGLE LOGIN RESPONSE ");

                AppPreference.getInstance().setLoginResponse(new Gson().toJson(loginResponse));
                SignInActivity.loginResponse = loginResponse;

                if (isEmpty(loginResponse.getPhone())) {
                    Toast.makeText(getApplicationContext(), "showPhoneLayout!", Toast.LENGTH_LONG).show();
                    showPhoneLayout();
                } else {
                    Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "GOOGLE LOGIN ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "ERROR!" + error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void updateProfile() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(SignInActivity.loginResponse.getToken());
        loginResponse.setPhone(phoneText.getText().toString());

        new UpdateUserRequest(loginResponse, new ICallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_LONG).show();
                Logger.debug(getClass(), "loginResponse ");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "updateProfile ERROR " + error.getErrorMessage());
            }
        }).execute();
    }

    private void facebookServerLogin(String id, String token) {
        //TEMPRORARY ADDING NUMBER!!!
        new LoginFacebookRequest(new LoginFacebook(id /*+ "228"*/, token), new ICallback<LoginResponse>() {

            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Logger.debug(getClass(), "FACEBOOK LOGIN RESPONSE ");

                AppPreference.getInstance().setLoginResponse(new Gson().toJson(loginResponse));
                SignInActivity.loginResponse = loginResponse;

                if (isEmpty(loginResponse.getPhone())) {
                    showPhoneLayout();
                } else {
                    Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "FACEBOOK LOGIN ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "ERROR!" + error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();

    }

    private void twitterServerLogin(String token, String secret) {
        new LoginTwitterRequest(new Session(token, secret), new ICallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {

                AppPreference.getInstance().setLoginResponse(new Gson().toJson(loginResponse));
                SignInActivity.loginResponse = loginResponse;

                if (isEmpty(loginResponse.getPhone())) {
                    Toast.makeText(getApplicationContext(), "showPhoneLayout!", Toast.LENGTH_LONG).show();
                    showPhoneLayout();
                } else {
                    Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "twitterServerLogin ERROR " + error.getErrorMessage());
            }
        }).execute();
    }

    private void showPhoneLayout() {
        Logger.debug(getClass(), "showPhoneLayout");
        findViewById(R.id.registerLayout).setVisibility(View.GONE);
        findViewById(R.id.socialLayout).setVisibility(View.GONE);
        LinearLayout enterPhoneLayout = (LinearLayout) findViewById(R.id.enterPhoneLayout);
        enterPhoneLayout.setVisibility(View.VISIBLE);
        phoneText = (EditText) enterPhoneLayout.findViewById(R.id.phone);
        Button sendPhoneButton = (Button) enterPhoneLayout.findViewById(R.id.sendPhoneButton);
        sendPhoneButton.setOnClickListener(this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Logger.debug(getClass(), "onConnectionFailed:" + connectionResult.getErrorMessage());
    }
}
