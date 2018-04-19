package com.quislisting.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpSendContactMessageRequestTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddListingActivity extends AppCompatActivity implements AsyncObjectResponse<Integer>,
        View.OnClickListener {

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.subject)
    EditText subject;
    @Bind(R.id.message)
    EditText message;
    @Bind(R.id.send)
    Button sendMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        setTitle(getString(R.string.addlistingactivitytitle));

        ButterKnife.bind(this);

        sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.send:
                final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                        Context.MODE_PRIVATE);
                final String language = sharedPreferences.getString("language", "en");
                final HttpSendContactMessageRequestTask updateUserRequestTask =
                        new HttpSendContactMessageRequestTask();
                updateUserRequestTask.delegate = this;
                updateUserRequestTask.execute(RestRouter.ContactCenter.SEND_CONTACT_MESSAGE,
                        email.getText().toString(), name.getText().toString(), subject.getText().toString(),
                        message.getText().toString(), language);
                break;
        }
    }

    @Override
    public void processFinish(final Integer result) {
        if (result != null && result == 200) {
            Toast.makeText(this, getString(R.string.contactemailsent),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.contactemailnotsent),
                    Toast.LENGTH_LONG).show();
        }
    }
}
