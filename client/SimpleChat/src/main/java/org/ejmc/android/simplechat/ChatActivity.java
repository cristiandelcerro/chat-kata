package org.ejmc.android.simplechat;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.Vector;

public class ChatActivity extends ListActivity {
    private ChatPresenter chatPresenter;
    private String userNameChat;

    public ChatActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (savedInstanceState == null) return;

        Vector<ChatMessage> messageList = new Vector<>();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ChatActivity.this.addMessages();
            }
        };

        Bundle bundle = getIntent().getExtras();
        userNameChat = bundle.getString("userName");

        chatPresenter = new ChatPresenter(userNameChat, this, handler, messageList);

        ListAdapter listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, messageList);
        setListAdapter(listAdapter);
    }

    public void onSendButtonClicked(View view) {
        EditText textToSend = (EditText)findViewById(R.id.textToSend);
        ChatMessage m = ChatMessage.messageFactory(userNameChat, textToSend.getText().toString());
        chatPresenter.sendMessage(m);
        textToSend.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        chatPresenter.start();
    }

    @Override
    public void onStop() {
        chatPresenter.stop();
        super.onStop();
    }

    public void addMessages() {
        onContentChanged();
    }

    void setChatPresenter(ChatPresenter chatPresenter) {
        this.chatPresenter = chatPresenter;
    }

    void setUserNameChat(String userNameChat) {
        this.userNameChat = userNameChat;
    }
}

