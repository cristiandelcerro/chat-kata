package org.ejmc.android.simplechat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Vector;

public class ChatActivity extends ListActivity {
    private ChatPresenter chatPresenter;
    private String userNameChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Vector<ChatMessage> messageList = new Vector<>();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ChatActivity.this.addMessages();
            }
        };

        chatPresenter = new ChatPresenter(this, handler, messageList);

        Bundle bundle = getIntent().getExtras();
        userNameChat = bundle.getString("userName");

        ListView chatListView = (ListView)findViewById(android.R.id.list);
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
    public void onStop() {
        chatPresenter.saveLastSeq();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        chatPresenter.finish();
        super.onDestroy();
    }

    public void addMessages() {
        onContentChanged();
    }

}

