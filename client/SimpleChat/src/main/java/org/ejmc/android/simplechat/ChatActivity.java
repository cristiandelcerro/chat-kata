package org.ejmc.android.simplechat;

import android.app.ListActivity;
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
    private static Vector<ChatMessage> messageList;
    private static String userNameChat;

    private ChatPresenter chatPresenter;
    private boolean testing;

    public ChatActivity() {
        super();
        this.testing = false;
    }

    public ChatActivity(boolean testing) {
        super();
        this.testing = testing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (testing) return;

        Handler handler = createHandler();
        setUserNameAndMessageList();
        createChatPresenterAndListAdapter(handler);
    }

    private Handler createHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ChatActivity.this.addMessages();
            }
        };

        return handler;
    }

    private void setUserNameAndMessageList() {
        Bundle bundle = getIntent().getExtras();
        String userNameChat = bundle.getString("userName");

        if (ChatActivity.userNameChat == null || !ChatActivity.userNameChat.equals(userNameChat)) {
            ChatActivity.userNameChat = userNameChat;
            messageList = new Vector<>();
        }
    }

    private void createChatPresenterAndListAdapter(Handler handler) {
        chatPresenter = new ChatPresenter(userNameChat, this, handler, messageList);

        ChatAdapter chatAdapter = new ChatAdapter(this, R.layout.chat_item, messageList);
        setListAdapter(chatAdapter);
    }

    public void onSendButtonClicked(View view) {
        EditText textToSend = (EditText) findViewById(R.id.textToSend);
        ChatMessage m = ChatMessage.messageFactory(userNameChat, textToSend.getText().toString());
        chatPresenter.sendMessage(m);
        textToSend.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        chatPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        chatPresenter.stop();
    }

    public void addMessages() {
        onContentChanged();
    }

    void setChatPresenter(ChatPresenter chatPresenter) {
        this.chatPresenter = chatPresenter;
    }

    void setUserNameChat(String userNameChat) {
        ChatActivity.userNameChat = userNameChat;
    }
}

