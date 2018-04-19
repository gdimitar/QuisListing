package com.quislisting.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.quislisting.R;
import com.quislisting.adapter.MessageOverviewAdapter;
import com.quislisting.model.Message;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetMessagesRequestTask;
import com.quislisting.task.impl.HttpSendMessageRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.FieldValidationUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageOverviewActivity extends AppCompatActivity implements AsyncCollectionResponse<Message>,
        AsyncObjectResponse<Integer>, View.OnClickListener {

    private String idToken = null;

    private ProgressDialog progressDialog;

    @Bind(R.id.messageEdit)
    EditText messageEdit;
    @Bind(R.id.chatSendButton)
    ImageButton chatSendButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_overview);
        setTitle(getString(R.string.messageoverviewactivitytitle));

        ButterKnife.bind(this);

        final String listingId = getIntent().getStringExtra("listingId");
        idToken = getIntent().getStringExtra("idToken");

        chatSendButton.setOnClickListener(this);

        if (StringUtils.isNotEmpty(listingId) && StringUtils.isNotEmpty(idToken)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fetchmessages));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            final HttpGetMessagesRequestTask getMessagesRequestTask =
                    new HttpGetMessagesRequestTask();
            getMessagesRequestTask.delegate = this;
            getMessagesRequestTask.execute(String.format(RestRouter.MessageCenter.GET_MESSAGE,
                    listingId), idToken);
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
    public void processFinish(final Collection<Message> messages) {
        if (CollectionUtils.isNotEmpty(messages)) {
            final ListView listView = (ListView) findViewById(R.id.conversation);
            final String userId = getIntent().getStringExtra("userId");
            final MessageOverviewAdapter messageOverviewAdapter = new MessageOverviewAdapter(this,
                    new ArrayList<>(messages), userId);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(messageOverviewAdapter);
        } else {
            final TextView noMessageText = (TextView) findViewById(R.id.noMessageText);
            noMessageText.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
    }

    @Override
    public void processFinish(final Integer result) {
        if (result != null && result == 200) {
            Toast.makeText(this, getString(R.string.messagesent),
                    Toast.LENGTH_SHORT).show();
            showReloadActivityDialog();
        } else
            Toast.makeText(this, getString(R.string.messagenotsent),
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.chatSendButton:
                if (!validateInputElements()) {
                    return;
                } else {
                    final HttpSendMessageRequestTask httpSendMessageRequestTask =
                            new HttpSendMessageRequestTask();
                    httpSendMessageRequestTask.delegate = this;
                    httpSendMessageRequestTask.execute(String.format(RestRouter.MessageCenter.
                                    GET_MESSAGE, getIntent().getStringExtra("messageOverviewId")),
                            messageEdit.getText().toString(), idToken);
                    showReloadActivityDialog();
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
