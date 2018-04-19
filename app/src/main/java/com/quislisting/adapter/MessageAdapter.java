package com.quislisting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.model.Message;
import com.quislisting.util.StringUtils;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private final Context context;
    private final List<Message> messages;
    private MessageAdapter.ViewHolder holder;

    public MessageAdapter(final Context context, final List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(final int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_messages, null);
            holder = new MessageAdapter.ViewHolder();
            holder.messageImage = (ImageView) convertView.findViewById(R.id.messageImage);
            holder.name = (TextView) convertView.findViewById(R.id.messageName);
            holder.text = (TextView) convertView.findViewById(R.id.messageText);
            holder.date = (TextView) convertView.findViewById(R.id.messageDate);
            convertView.setTag(holder);
        } else {
            holder = (MessageAdapter.ViewHolder) convertView.getTag();
        }

        holder.messageImage.setImageResource(R.drawable.noimage);
        holder.name.setText(getName(messages.get(position)));
        holder.text.setText(modifyTextFromMessage(messages.get(position).getText()));
        holder.date.setText(StringUtils.convertTimestampToString(messages.get(position).getCreated()));

        return convertView;
    }

    private String getName(final Message message) {
        if (message != null) {
            return message.getSender().getFirstName() + StringUtils.SEPARATOR
                    + message.getSender().getLastName();
        }

        return StringUtils.EMPTY_STRING;
    }

    private String modifyTextFromMessage(final String message) {
        if (StringUtils.isEmpty(message)) {
            return StringUtils.EMPTY_STRING;
        }

        final String[] stringArray = message.split("\\s+");

        if (stringArray.length == 1) {
            return stringArray[0] + StringUtils.THREE_DOTS_SEPARATOR;
        } else {
            return stringArray[0] + stringArray[1] + StringUtils.THREE_DOTS_SEPARATOR;
        }
    }

    private static class ViewHolder {
        private ImageView messageImage;
        private TextView name, text, date;
    }
}
