package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quislisting.R;
import com.quislisting.model.AuthenticationResult;
import com.quislisting.model.request.AuthenticateUserRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.ConnectionChecker;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.PasswordStrengthUtil;
import com.quislisting.util.StringUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_SIGNUP = 0;
    private static final String MAIN_VIEW = "mainView";
    private static final String ADD_LISTING_VIEW = "addListingView";
    private static final String LISTINGS_VIEW = "listingsView";
    private static final String MESSAGES_VIEW = "messagesView";
    private static final int SIGN_IN_CODE = 777;

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.signup)
    TextView signup;
    @Bind(R.id.clearText)
    Button clearText;
    @Bind(R.id.facebookLogin)
    LoginButton facebookLogin;
    @Bind(R.id.twitterLogin)
    TwitterLoginButton twitterLogin;
    @Bind(R.id.googleLogin)
    SignInButton googleLogin;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.progressBarText)
    TextView progressBarText;

    private ProgressDialog loginProgressDialog;
    private ProgressDialog facebookProgressDialog;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.loginactivitytitle));

        ButterKnife.bind(this);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        username.addTextChangedListener(this);
        clearText.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        twitterLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);

        loginProgressDialog = new ProgressDialog(this);
        loginProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginProgressDialog.setCancelable(false);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count,
                                          final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before,
                                      final int count) {
                updatePasswordStrengthView(s.toString());
            }

            @Override
            public void afterTextChanged(final Editable s) {
            }
        });
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBarText.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.signup:
                final Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.clearText:
                username.setText(StringUtils.EMPTY_STRING);
                break;
            case R.id.facebookLogin:
                facebookLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                facebookProgressDialog = new ProgressDialog(LoginActivity.this);
                                facebookProgressDialog.setMessage("Retrieving data...");
                                facebookProgressDialog.show();

                                final String accessToken = loginResult.getAccessToken().getToken();

                                final GraphRequest graphRequest = GraphRequest
                                        .newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                                            facebookProgressDialog.dismiss();
                                            getFacebookData(object);
                                        });
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(LoginActivity.this,
                                        R.string.facebookconnectioncancelled, Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onError(final FacebookException exception) {
                                Toast.makeText(LoginActivity.this,
                                        R.string.facebookconnectionerror, Toast.LENGTH_SHORT).show();
                            }
                        });

                break;
            case R.id.twitterLogin:
                Twitter.initialize(this);
                twitterLogin.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(final Result<TwitterSession> result) {
                        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                                .getActiveSession();
                        final TwitterAuthToken authToken = session.getAuthToken();
                        final String token = authToken.token;
                        final String secret = authToken.secret;

                        twitterLogin(session);
                    }

                    @Override
                    public void failure(final TwitterException exception) {
                        Toast.makeText(LoginActivity.this, R.string.twitterconnectionerror,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.googleLogin:
                final GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
                final Intent googleIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(googleIntent, SIGN_IN_CODE);
                break;
        }
    }

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }

        if (requestCode == SIGN_IN_CODE) {
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int start, final int before,
                                  final int count) {

    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int start, final int before,
                              final int count) {
        if (!username.getText().toString().equals(StringUtils.EMPTY_STRING)) {
            clearText.setVisibility(View.VISIBLE);
        } else {
            clearText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(final Editable editable) {

    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(final @NonNull ConnectionResult connectionResult) {

    }

    private void getFacebookData(final JSONObject object) {
        System.out.println(object);
    }

    private void twitterLogin(final TwitterSession session) {
        System.out.println(session);
    }

    private void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        login.setEnabled(false);
        loginProgressDialog.setMessage(getString(R.string.authenticatinguser));
        loginProgressDialog.show();

        if (ConnectionChecker.isOnline()) {
            final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            final AuthenticateUserRequest authenticateUserRequest =
                    new AuthenticateUserRequest(username.getText().toString(), password.getText().toString(),
                            false);
            final Call<AuthenticationResult> authenticationResultCall = apiInterface.authenticateUser(authenticateUserRequest);
            authenticationResultCall.enqueue(new Callback<AuthenticationResult>() {
                @Override
                public void success(final Result<AuthenticationResult> result) {
                    Intent intent = null;
                    final String fromView = getIntent().getStringExtra("fromView");

                    switch (fromView) {
                        case MAIN_VIEW:
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            break;
                        case ADD_LISTING_VIEW:
                            intent = new Intent(getApplicationContext(), AddListingActivity.class);
                            break;
                        case LISTINGS_VIEW:
                            intent = new Intent(getApplicationContext(), ListingsActivity.class);
                            break;
                        case MESSAGES_VIEW:
                            intent = new Intent(getApplicationContext(), MessagesActivity.class);
                            break;
                    }

                    assert intent != null;
                    intent.putExtra("username", username.getText().toString());

                    if (result != null && StringUtils.isNotEmpty(result.data.getId_token())) {
                        intent.putExtra("idToken", result.data.getId_token());
                        onLoginSuccess();
                        startActivity(intent);
                    } else {
                        onLoginFailed();
                    }
                }

                @Override
                public void failure(final TwitterException exception) {
                    onLoginFailed();
                }
            });
        } else {
            onLoginFailed();
        }
        loginProgressDialog.dismiss();
    }

    private void handleSignInResult(final GoogleSignInResult result) {
        if (result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(this, R.string.googleconnectionerror, Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onLoginSuccess() {
        login.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.loginfailed),
                Toast.LENGTH_SHORT).show();
        login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        final String usernameText = username.getText().toString();

        final String errorCode = FieldValidationUtils.validateEmailFieldValue(usernameText,
                getString(R.string.emailvalidation));

        if (StringUtils.isNotEmpty(errorCode)) {
            username.setError(errorCode);
            valid = false;
        } else {
            username.setError(null);
        }

        return valid;
    }

    private void updatePasswordStrengthView(final String password) {
        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        if (TextView.VISIBLE != progressBarText.getVisibility())
            return;

        if (password.isEmpty()) {
            progressBarText.setText(StringUtils.EMPTY_STRING);
            progressBar.setProgress(0);
            return;
        }

        final PasswordStrengthUtil str = PasswordStrengthUtil.calculateStrength(password);
        progressBarText.setText(str.getText(this));
        progressBarText.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals(getString(R.string.password_strength_weak))) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals(getString(R.string.password_strength_medium))) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals(getString(R.string.password_strength_strong))) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }
}
