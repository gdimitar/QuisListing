package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.quislisting.model.request.RegisterUserRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGN_IN_CODE = 777;

    @Bind(R.id.register)
    Button register;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.firstName)
    EditText firstName;
    @Bind(R.id.lastName)
    EditText lastName;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.clearEmail)
    Button clearEmail;
    @Bind(R.id.clearFirstName)
    Button clearFirstName;
    @Bind(R.id.clearLastName)
    Button clearLastName;
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

    private ProgressDialog progressDialog;
    private ProgressDialog facebookProgressDialog;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(getString(R.string.signupactivitytitle));

        ButterKnife.bind(this);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        clearEmail.setOnClickListener(this);
        clearFirstName.setOnClickListener(this);
        clearLastName.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        twitterLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);

        email.addTextChangedListener(new AddListenerOnTextChange(this, email, clearEmail));
        firstName.addTextChangedListener(new AddListenerOnTextChange(this, firstName,
                clearFirstName));
        lastName.addTextChangedListener(new AddListenerOnTextChange(this, lastName,
                clearLastName));

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
            case R.id.register:
                signup();
                break;
            case R.id.login:
                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.clearEmail:
                email.setText(StringUtils.EMPTY_STRING);
                break;
            case R.id.clearFirstName:
                firstName.setText(StringUtils.EMPTY_STRING);
                break;
            case R.id.clearLastName:
                lastName.setText(StringUtils.EMPTY_STRING);
                break;
            case R.id.facebookLogin:
                facebookLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                facebookProgressDialog = new ProgressDialog(SignupActivity.this);
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
                                Toast.makeText(SignupActivity.this,
                                        R.string.facebookconnectioncancelled, Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onError(final FacebookException exception) {
                                Toast.makeText(SignupActivity.this,
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
                        Toast.makeText(SignupActivity.this, R.string.twitterconnectionerror,
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
    public void onConnectionFailed(final @NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == SIGN_IN_CODE) {
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class AddListenerOnTextChange implements TextWatcher {
        private final Context context;
        private final EditText editText;
        private final Button button;

        public AddListenerOnTextChange(final Context context, final EditText editText,
                                       final Button button) {
            super();
            this.context = context;
            this.editText = editText;
            this.button = button;
        }

        @Override
        public void afterTextChanged(final Editable s) {
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int before,
                                      final int count) {
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before,
                                  final int count) {
            if (!editText.getText().toString().equals(StringUtils.EMPTY_STRING)) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getFacebookData(final JSONObject object) {
        System.out.println(object);
    }

    private void twitterLogin(final TwitterSession session) {
        System.out.println(session);
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

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        register.setEnabled(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.creatingaccount));
        progressDialog.show();

        final String emailText = email.getText().toString();
        final String firstNameText = firstName.getText().toString();
        final String lastNameText = lastName.getText().toString();
        final String passwordText = password.getText().toString();

        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        final String language = sharedPreferences.getString("language", null);

        final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        final RegisterUserRequest registerUserRequest = new RegisterUserRequest(emailText, firstNameText,
                lastNameText, emailText, true, language, passwordText);
        final Call<Integer> registerUserCall = apiInterface.registerUser(registerUserRequest);

        registerUserCall.enqueue(new Callback<Integer>() {
            @Override
            public void success(final Result<Integer> result) {
                if (result != null && result.data != null && result.data == 201) {
                    onSignupSuccess();
                } else {
                    onSignupFailed();
                }
            }

            @Override
            public void failure(final TwitterException exception) {
                Toast.makeText(getBaseContext(), getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), getString(R.string.loginsuccessful), Toast.LENGTH_SHORT).show();

        register.setEnabled(true);
        progressDialog.dismiss();
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.loginfailed), Toast.LENGTH_SHORT).show();

        progressDialog.dismiss();
        register.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        final String emailText = email.getText().toString();
        final String firstNameText = firstName.getText().toString();
        final String lastNameText = lastName.getText().toString();

        final String errorCodeEmail = FieldValidationUtils.validateEmailFieldValue(emailText,
                getString(R.string.emailvalidation));

        if (StringUtils.isNotEmpty(errorCodeEmail)) {
            email.setError(errorCodeEmail);
            valid = false;
        } else {
            email.setError(null);
        }

        final String errorCodeFirstName = FieldValidationUtils.validateFieldValue(firstNameText,
                getString(R.string.firstnamevalidation), 15);

        if (StringUtils.isNotEmpty(errorCodeFirstName)) {
            firstName.setError(errorCodeFirstName);
            valid = false;
        } else {
            firstName.setError(null);
        }

        final String errorCodeLastName = FieldValidationUtils.validateFieldValue(lastNameText,
                getString(R.string.lastnamevalidation), 15);

        if (StringUtils.isNotEmpty(errorCodeLastName)) {
            lastName.setError(errorCodeLastName);
            valid = false;
        } else {
            lastName.setError(null);
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
