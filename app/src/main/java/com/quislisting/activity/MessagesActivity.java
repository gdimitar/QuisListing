package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.adapter.MessageAdapter;
import com.quislisting.model.Message;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetMessagesRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessagesActivity extends AppCompatActivity implements AsyncCollectionResponse<Message> {

    private String idToken;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        idToken = getIntent().getStringExtra("idToken");
        if (StringUtils.isEmpty(idToken)) {
            final Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("fromView", "messagesView");
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle(getString(R.string.messagesactivitytitle));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetchmessages));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (StringUtils.isNotEmpty(idToken)) {
            final HttpGetMessagesRequestTask getMessagesRequestTask =
                    new HttpGetMessagesRequestTask();
            getMessagesRequestTask.delegate = this;
            getMessagesRequestTask.execute(RestRouter.MessageCenter.GET_MESSAGES, idToken);
        }
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
                final Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idToken", this.idToken);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void processFinish(final Collection<Message> messages) {
        if (CollectionUtils.isNotEmpty(messages)) {
            final List<Message> messageList = new ArrayList<>(messages);
            final ListView listView = (ListView) findViewById(R.id.messages);
            final MessageAdapter messageAdapter = new MessageAdapter(this,
                    new ArrayList<>(messages));
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(messageAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                final Intent intent = new Intent(this, MessageOverviewActivity.class);
                intent.putExtra("listingId", String.valueOf(messageList.get(position).getListingId()));
                intent.putExtra("idToken", this.idToken);
                intent.putExtra("messageOverviewId", messageList.get(position).getId());
                intent.putExtra("userId", String.valueOf(messageList.get(position)
                        .getSender().getId()));
                startActivity(intent);
            });
        } else {
            final TextView noMessagesText = (TextView) findViewById(R.id.noMessagesText);
            noMessagesText.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
    }
}
