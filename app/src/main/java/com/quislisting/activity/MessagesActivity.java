package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.adapter.MessageAdapter;
import com.quislisting.model.Message;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {

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
            final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            final Call<Collection<Message>> getMessagesCall = apiInterface.getMessages("Bearer " + idToken);
            getMessagesCall.enqueue(new Callback<Collection<Message>>() {
                @Override
                public void onResponse(final Call<Collection<Message>> call,
                                       final Response<Collection<Message>> response) {
                    if (CollectionUtils.isNotEmpty(response.body())) {
                        final List<Message> messageList = new ArrayList<>(response.body());
                        final ListView listView = (ListView) findViewById(R.id.messages);
                        final MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),
                                new ArrayList<>(response.body()));
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(messageAdapter);

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            final Intent intent = new Intent(getApplicationContext(), MessageOverviewActivity.class);
                            intent.putExtra("listingId", String.valueOf(messageList.get(position).getListingId()));
                            intent.putExtra("idToken", idToken);
                            intent.putExtra("messageOverviewId", messageList.get(position).getId());
                            intent.putExtra("userId", String.valueOf(messageList.get(position)
                                    .getSender().getId()));
                            startActivity(intent);
                        });
                    } else {
                        final TextView noMessagesText = (TextView) findViewById(R.id.noMessagesText);
                        noMessagesText.setVisibility(View.VISIBLE);
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
}
