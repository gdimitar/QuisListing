package com.quislisting.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.model.Message;
import com.quislisting.util.StringUtils;

import java.util.List;

public class MessageOverviewAdapter extends BaseAdapter {

    private final Context context;
    private final List<Message> messages;
    private final String userId;
    private MessageOverviewAdapter.ViewHolder holder;

    public MessageOverviewAdapter(final Context context, final List<Message> messages,
                                  final String userId) {
        this.context = context;
        this.messages = messages;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    @Override
    public Message getItem(final int position) {
        if (messages != null) {
            return messages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_message_overview, null);
            holder = new MessageOverviewAdapter.ViewHolder();
            holder.txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
            holder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            holder.contentWithBG = (LinearLayout) convertView.findViewById(R.id.contentWithBackground);
            convertView.setTag(holder);
        } else {
            holder = (MessageOverviewAdapter.ViewHolder) convertView.getTag();
        }

        final boolean isMe = String.valueOf(messages.get(position).getSender().getId()).equals(userId);

        setAlignment(holder, isMe);
        holder.txtInfo.setText(StringUtils.convertTimestampToString(messages.get(position)
                .getCreated()));
        holder.txtMessage.setText(messages.get(position).getText());

        return convertView;
    }

    private void setAlignment(final ViewHolder holder, final boolean isMe) {
        if (!isMe) {
//            holder.contentWithBG.setBackgroundResource(R.drawable.in_message);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.END;
            holder.contentWithBG.setLayoutParams(layoutParams);

            final RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.END;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.END;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
//            holder.contentWithBG.setBackgroundResource(R.drawable.out_message);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.START;
            holder.contentWithBG.setLayoutParams(layoutParams);

            final RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.START;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.START;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private static class ViewHolder {
        private TextView txtInfo;
        private TextView txtMessage;
        private LinearLayout content;
        private LinearLayout contentWithBG;
    }
}
