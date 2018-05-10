package com.quislisting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.model.User;
import com.quislisting.model.request.UpdateUserRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.firstName)
    EditText firstName;
    @Bind(R.id.lastName)
    EditText lastName;
    @Bind(R.id.receiveUpdates)
    CheckBox receiveUpdates;
    @Bind(R.id.language)
    Spinner language;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.profilePreferencesView)
    ScrollView scrollView;

    private String idToken = null;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.profileactivitytitle));

        idToken = getIntent().getStringExtra("idToken");

        if (StringUtils.isNotEmpty(idToken)) {
            ButterKnife.bind(this);

            save.setOnClickListener(this);

            language.setOnItemSelectedListener(this);

            apiInterface = APIClient.getClient().create(APIInterface.class);
            final Call<User> getUserCall = apiInterface.getUser("Bearer " + idToken);
            getUserCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(final Call<User> call, final Response<User> response) {
                    if (response.isSuccessful()) {
                        email.setText(response.body().getEmail());
                        firstName.setText(response.body().getFirstName());
                        lastName.setText(response.body().getLastName());
                        receiveUpdates.setChecked(response.body().getUpdates());
                        language.setSelection(setLanguageByPosition(response.body().getLangKey()));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.retrieveusererror),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(final Call<User> call, final Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            scrollView.removeAllViews();
            setContentView(R.layout.empty_layout);

            Toast.makeText(getApplicationContext(), getString(R.string.retrieveprofileprefencesfailed),
                    Toast.LENGTH_SHORT).show();

            final TextView emptyText = (TextView) findViewById(R.id.emptyText);
            emptyText.setText(getString(R.string.preferencesnotavailable));
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.save:
                if (!validateInputElements()) {
                    return;
                } else {
                    final String idToken = getIntent().getStringExtra("idToken");
                    if (StringUtils.isNotEmpty(idToken)) {
                        final UpdateUserRequest updateUserRequest =
                                new UpdateUserRequest(email.getText().toString(),
                                        firstName.getText().toString(), lastName.getText().toString(),
                                        email.getText().toString(), true,
                                        getLanguage(language.getSelectedItemId()));
                        final Call<String> updateUserCall = apiInterface.updateUser(updateUserRequest,
                                "Bearer " + idToken);

                        updateUserCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(final Call<String> call, final Response<String> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.profileupdated),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.profilenotupdated),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(final Call<String> call, final Throwable t) {
                                call.cancel();
                                Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                               final long id) {

    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idToken", this.idToken);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.putExtra("idToken", this.idToken);
                startActivity(intentHome);
                break;
        }
        return true;
    }

    private boolean validateInputElements() {
        boolean valid = true;

        final String firstNameText = firstName.getText().toString();
        final String lastNameText = lastName.getText().toString();

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

    private int setLanguageByPosition(final String language) {
        switch (language) {
            case "en":
                return 0;
            case "bg":
                return 1;
            case "ro":
                return 2;
        }
        return 0;
    }

    private String getLanguage(final long languageId) {
        switch ((int) languageId) {
            case 0:
                return "en";
            case 1:
                return "bg";
            case 2:
                return "ro";
        }
        return "en";
    }
}
