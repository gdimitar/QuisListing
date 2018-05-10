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
import com.quislisting.model.request.ContactMessageRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddListingActivity extends AppCompatActivity implements View.OnClickListener {

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

    private APIInterface apiInterface;

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

                apiInterface = APIClient.getClient().create(APIInterface.class);

                final ContactMessageRequest contactMessageRequest =
                        new ContactMessageRequest(email.getText().toString(), name.getText().toString(),
                                subject.getText().toString(), message.getText().toString(), language);
                final Call<Integer> sendContactMessageCall = apiInterface.sendContactMessage(contactMessageRequest);
                sendContactMessageCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(final Call<Integer> call, final Response<Integer> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.contactemailsent),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.contactemailnotsent),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(final Call<Integer> call, final Throwable t) {
                        call.cancel();
                        Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
}
