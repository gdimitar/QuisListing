package com.quislisting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.model.User;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.AsyncSecondObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetUserRequestTask;
import com.quislisting.task.impl.HttpUpdateUserRequestTask;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements AsyncObjectResponse<User>,
        AsyncSecondObjectResponse<Integer>, View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    private String idToken = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.profileactivitytitle));

        // create toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        save.setOnClickListener(this);

        language.setOnItemSelectedListener(this);

        idToken = getIntent().getStringExtra("idToken");
        if (StringUtils.isNotEmpty(idToken)) {
            final HttpGetUserRequestTask getUserRequestTask =
                    new HttpGetUserRequestTask();
            getUserRequestTask.delegate = this;
            getUserRequestTask.execute(RestRouter.User.GET_USER, idToken);
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
                        final HttpUpdateUserRequestTask updateUserRequestTask =
                                new HttpUpdateUserRequestTask(receiveUpdates.isChecked());
                        updateUserRequestTask.additionalDelegate = this;
                        updateUserRequestTask.execute(RestRouter.User.UPDATE_USER,
                                email.getText().toString(), firstName.getText().toString(),
                                lastName.getText().toString(), language.getSelectedItem().toString(),
                                idToken);
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
    public void processFinish(final User user) {
        if (user != null) {
            email.setText(user.getEmail());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            receiveUpdates.setChecked(user.getUpdates());
            language.setSelection(setLanguageByPosition(user.getLangKey()));
        }
    }

    @Override
    public void processSecondFinish(final Integer result) {
        if (result != null && result == 200) {
            Toast.makeText(this, getString(R.string.profileupdated),
                    Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, getString(R.string.profilenotupdated),
                    Toast.LENGTH_LONG).show();
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
}
