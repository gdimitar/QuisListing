package com.quislisting.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.QuisListingApplication;
import com.quislisting.R;
import com.quislisting.adapter.MessageOverviewAdapter;
import com.quislisting.model.Message;
import com.quislisting.model.request.MessageRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.ConnectionChecker;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageOverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private String idToken = null;
    private String listingId = null;

    private ProgressDialog progressDialog;

    @Bind(R.id.messageEdit)
    EditText messageEdit;
    @Bind(R.id.chatSendButton)
    ImageButton chatSendButton;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_overview);
        setTitle(getString(R.string.messageoverviewactivitytitle));

        ButterKnife.bind(this);

        listingId = getIntent().getStringExtra("listingId");
        idToken = getIntent().getStringExtra("idToken");

        chatSendButton.setOnClickListener(this);

        if (StringUtils.isNotEmpty(listingId) && StringUtils.isNotEmpty(idToken)
                && ConnectionChecker.isOnline()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fetchmessages));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            final Call<Collection<Message>> getMessagesCall = apiInterface.getMessages("Bearer " + idToken);
            getMessagesCall.enqueue(new Callback<Collection<Message>>() {
                @Override
                public void onResponse(final Call<Collection<Message>> call,
                                       final Response<Collection<Message>> response) {
                    if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                        final ListView listView = (ListView) findViewById(R.id.conversation);
                        final String userId = getIntent().getStringExtra("userId");
                        final MessageOverviewAdapter messageOverviewAdapter = new MessageOverviewAdapter(getApplicationContext(),
                                new ArrayList<>(response.body()), userId);
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(messageOverviewAdapter);
                    } else {
                        final TextView noMessageText = (TextView) findViewById(R.id.noMessageText);
                        noMessageText.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.retrievemessageerror),
                                Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(final Call<Collection<Message>> call, final Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra("idToken", this.idToken);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(this, MessagesActivity.class);
                intent.putExtra("idToken", this.idToken);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.chatSendButton:
                if (!validateInputElements()) {
                    return;
                } else {
                    final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                            Context.MODE_PRIVATE);
                    final String language = sharedPreferences.getString("language", null);

                    final String senderName = ((QuisListingApplication) this.getApplication()).getName();
                    final String senderEmail = ((QuisListingApplication) this.getApplication()).getEmail();

                    final MessageRequest messageRequest =
                            new MessageRequest(senderName, senderEmail,
                                    messageEdit.getText().toString(), language);
                    final Call<Integer> sendMessageCall = apiInterface.sendMessage(listingId,
                            messageRequest);
                    sendMessageCall.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(final Call<Integer> call, final Response<Integer> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), getString(R.string.messagesent),
                                        Toast.LENGTH_SHORT).show();
                                showReloadActivityDialog();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.messagenotsent),
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
                break;
        }
    }

    private void showReloadActivityDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.alertdialogmessage))
                .setCancelable(false).setPositiveButton(getString(R.string.alertdialogyes),
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                    startActivity(getIntent());
                }).setNegativeButton(getString(R.string.alertdialogno),
                (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.alertdialogtitle));
        alertDialog.show();
    }

    private boolean validateInputElements() {
        boolean valid = true;

        final String messageText = messageEdit.getText().toString();
        final String errorCode = FieldValidationUtils.validateFieldValue(messageText,
                getString(R.string.messagetextvalidation), 100);
        if (StringUtils.isNotEmpty(errorCode)) {
            messageEdit.setError(errorCode);
            valid = false;
        } else {
            messageEdit.setError(null);
        }

        return valid;
    }
}
