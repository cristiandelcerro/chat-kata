package org.ejmc.android.simplechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

public class ChatAdapter extends ArrayAdapter {
    private Vector<ChatMessage> messageList;

    public ChatAdapter(Context context, int textViewResourceId, Vector<ChatMessage> messageList) {
        super(context, textViewResourceId, messageList);
        this.messageList = messageList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Keeps reference to avoid future findViewById()
        ChatMessageViewHolder viewHolder;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.chat_item, parent, false);

            viewHolder = new ChatMessageViewHolder();
            viewHolder.userName = (TextView) view.findViewById(R.id.userNameItem);
            viewHolder.messageFromUser = (TextView) view.findViewById(R.id.messageItem);

            view.setTag(viewHolder);
        }

        else
            viewHolder = (ChatMessageViewHolder) view.getTag();

        ChatMessage chatMessage = messageList.get(position);
        if (chatMessage != null) {
            viewHolder.userName.setText(chatMessage.getNick());
            viewHolder.messageFromUser.setText(chatMessage.getMessage());
        }
        return view;
    }

    class ChatMessageViewHolder {
        TextView userName;
        TextView messageFromUser;
    }

}
