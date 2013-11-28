package org.ejmc.android.simplechat;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatPresenter {
//    public static final String server = "http://172.16.100.149:8080/chat-kata/api/chat";
    public static final String server = "http://172.16.100.149:8080/chat-kata/api/chat";
    public static final String savedProperties = "savedProperties";

    private ChatActivity chatActivity;
    private ReceiverTimer receiverTimer;
    private SenderTimer senderTimer;
    private ConcurrentLinkedQueue<ChatMessage> messagesToSend;
    private boolean finished;
    private Handler chatActivityHandler;
    private Vector<ChatMessage> messageList;
    private int lastSeq;

    public ChatPresenter(ChatActivity chatActivity, Handler chatActivityHandler, Vector<ChatMessage> messageList) {
        this.finished = false;
        this.chatActivity = chatActivity;
        this.chatActivityHandler = chatActivityHandler;
        this.messageList = messageList;
        restoreLastSeq();

        messagesToSend = new ConcurrentLinkedQueue<>();

        senderTimer = new SenderTimer(messagesToSend);
        receiverTimer = new ReceiverTimer(this);
    }

    public void sendMessage(ChatMessage message) {
        if(!message.getMessage().equals(""))
            messagesToSend.add(message);
    }

    public void receiveMessages(ServerResponse serverResponse) {
        synchronized (this) {
            if (finished) return;
            lastSeq = serverResponse.getNextSeq();
        }

        messageList.addAll(serverResponse.getMessages());
        Message msg = Message.obtain();
        chatActivityHandler.sendMessage(msg);
    }

    public void finish() {
        synchronized (this) {
            finished = true;
        }

        receiverTimer.cancel();
        senderTimer.cancel();
    }

    synchronized int getLastSeq() {
        return lastSeq;
    }

    public void saveLastSeq() {
        SharedPreferences settings = chatActivity.getSharedPreferences(savedProperties, ChatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("lastSeq", lastSeq);
        editor.commit();
    }

    private void restoreLastSeq() {
        SharedPreferences settings = chatActivity.getSharedPreferences(savedProperties, ChatActivity.MODE_PRIVATE);
        this.lastSeq = settings.getInt("lastSeq", 0);
    }
}