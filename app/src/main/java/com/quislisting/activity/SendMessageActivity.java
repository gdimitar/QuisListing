package com.quislisting.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.model.request.MessageRequest;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.ButtonUtils;
import com.quislisting.util.ConnectionChecker;
import com.quislisting.util.EditTextUtils;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.StringUtils;
import com.quislisting.util.TextInputLayoutUtils;
import com.quislisting.util.TextViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.sendMessageLayout)
    LinearLayout sendMessageLayout;

    private EditText nameEditText = null;
    private EditText emailEditText = null;
    private EditText messageEditText = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        setTitle(getString(R.string.sendmessageactivitytitle));

        ButterKnife.bind(this);

        final String idToken = getIntent().getStringExtra("idToken");
        if (StringUtils.isEmpty(idToken)) {
            nameEditText = EditTextUtils.createEditText(this, getString(R.string.name),
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    R.id.message_name, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                            | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                    false, 100, 2, 4);

            final TextInputLayout nameTextInputLayout = TextInputLayoutUtils.createTextInputLayout(this, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 10, 10);
            nameTextInputLayout.addView(nameEditText);

            emailEditText = EditTextUtils.createEditText(this, getString(R.string.email), LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, R.id.message_email,
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                            | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, false,
                    100, 2, 4);

            final TextInputLayout emailTextInputLayout =
                    TextInputLayoutUtils.createTextInputLayout(this, LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 10, 10);
            emailTextInputLayout.addView(emailEditText);

            sendMessageLayout.addView(nameTextInputLayout);
            sendMessageLayout.addView(emailTextInputLayout);
        }

        messageEditText = EditTextUtils.createEditText(this, getString(R.string.message),
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, R.id.message_text,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, true,
                100, 2, 4);

        final TextInputLayout messageTextInputLayout = TextInputLayoutUtils.createTextInputLayout(this, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 10, 10);

        final TextView messageTextViewInfo = TextViewUtils.createTextView(this, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, R.id.message_text_info, R.string.messageinfo);

        messageTextInputLayout.addView(messageEditText);
        messageTextInputLayout.addView(messageTextViewInfo);

        sendMessageLayout.addView(messageTextInputLayout);

        final AppCompatButton sendMessage = ButtonUtils.createAppCompatButton(this,
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                R.id.send_message, 24, 24, R.string.send);

        sendMessageLayout.addView(sendMessage);

        assert sendMessage != null;
        sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.send_message:
                if (!validateInputElements()) {
                    return;
                } else if (ConnectionChecker.isOnline()) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage(R.string.registernewusertext).setCancelable(false)
                            .setPositiveButton(getString(R.string.alertdialogyes),
                                    (DialogInterface.OnClickListener) (dialog, which) -> {
                                        final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

                                        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                                                Context.MODE_PRIVATE);
                                        final String language = sharedPreferences.getString("language", null);

                                        final MessageRequest messageRequest = new MessageRequest(nameEditText.getText().toString(),
                                                emailEditText.getText().toString(), messageEditText.getText().toString(),
                                                language);
                                        final Call<Integer> sendContactMessageCall =
                                                apiInterface.sendMessage(getIntent().getStringExtra("listingId"),
                                                        messageRequest);

                                        sendContactMessageCall.enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(final Call<Integer> call, final Response<Integer> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.messagesent),
                                                            Toast.LENGTH_SHORT).show();
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
                                    }).setNegativeButton(getString(R.string.alertdialogno),
                            (DialogInterface.OnClickListener) (dialog, which) -> {
                                dialog.cancel();
                            });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setTitle(getString(R.string.alertdialognewuser));
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, ListingDetailsActivity.class);
        intent.putExtra("idToken", getIntent().getStringExtra("idToken"));
        intent.putExtra("listingId", getIntent().getStringExtra("listingId"));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(this, ListingDetailsActivity.class);
                intent.putExtra("idToken", getIntent().getStringExtra("idToken"));
                intent.putExtra("listingId", getIntent().getStringExtra("listingId"));
                startActivity(intent);
                break;
        }
        return true;
    }

    private boolean validateInputElements() {
        boolean valid = true;

        if (nameEditText != null && emailEditText != null) {
            final String nameText = nameEditText.getText().toString();
            final String emailText = emailEditText.getText().toString();

            final String errorCodeName = FieldValidationUtils.validateFieldValue(nameText,
                    getString(R.string.messagenamevalidation), 20);
            if (StringUtils.isNotEmpty(errorCodeName)) {
                nameEditText.setError(errorCodeName);
                valid = false;
            } else {
                nameEditText.setError(null);
            }

            final String errorCodeEmail = FieldValidationUtils.validateEmailFieldValue(emailText,
                    getString(R.string.emailvalidation));
            if (StringUtils.isNotEmpty(errorCodeEmail)) {
                emailEditText.setError(errorCodeEmail);
                valid = false;
            } else {
                emailEditText.setError(null);
            }
        }

        final String messageText = messageEditText.getText().toString();
        final String errorCode = FieldValidationUtils.validateFieldValue(messageText,
                getString(R.string.messagetextvalidation), 100);
        if (StringUtils.isNotEmpty(errorCode)) {
            messageEditText.setError(errorCode);
            valid = false;
        } else {
            messageEditText.setError(null);
        }

        return valid;
    }
}
