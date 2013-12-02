package org.ejmc.android.simplechat;

import android.content.SharedPreferences;
import android.os.Handler;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatPresenter {
    public static final String server = "http://172.16.100.50:8080/chat-kata/api/chat";
    public static final String savedProperties = "savedProperties";

    private String userName;
    private ChatActivity chatActivity;
    private ReceiverTimer receiverTimer;
    private SenderThread senderThread;
    private boolean stopped;
    private Handler chatActivityHandler;
    private Vector<ChatMessage> messageList;
    private int lastSeq;

    public ChatPresenter(String userName, ChatActivity chatActivity, Handler chatActivityHandler, Vector<ChatMessage> messageList) {
        this.userName = userName;
        this.chatActivity = chatActivity;
        this.chatActivityHandler = chatActivityHandler;
        this.messageList = messageList;
        stopped = true;
    }

    private void createTimers() {
        senderThread = new SenderThread();
        senderThread.start();
        receiverTimer = new ReceiverTimer(this);
    }

    synchronized void start() {
        if (!stopped) return;
        stopped = false;

        restoreLastSeq();
        createTimers();
    }

    synchronized void stop() {
        if (stopped) return;
        stopped = true;

        destroyTimers();
        saveLastSeq();
    }

    public void sendMessage(ChatMessage message) {
        if(!message.getMessage().equals("") && !message.getNick().equals(""))
            senderThread.prepareMessageToSend(message);
    }

    void receiveMessages(ServerResponse serverResponse) {
        synchronized (this) {
            if (stopped) return;
            lastSeq = serverResponse.getNextSeq();
        }

        messageList.addAll(serverResponse.getMessages());
        chatActivityHandler.sendEmptyMessage(0);
    }

    private void destroyTimers() {
        senderThread.finish();
        receiverTimer.cancel();

        try {
            senderThread.join();
        }

        catch (InterruptedException e) {
            System.err.println("El hilo enviador ha terminado mal.");
            System.err.print(e.toString());
        }
    }

    synchronized int getLastSeq() {
        return lastSeq;
    }

    private void saveLastSeq() {
        SharedPreferences settings = chatActivity.getSharedPreferences(savedProperties, ChatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(userName, lastSeq);
        editor.commit();
    }

    private void restoreLastSeq() {
        SharedPreferences settings = chatActivity.getSharedPreferences(savedProperties, ChatActivity.MODE_PRIVATE);
        this.lastSeq = settings.getInt(userName, 0);
    }

    synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    void setReceiverTimer(ReceiverTimer receiverTimer) {
        this.receiverTimer = receiverTimer;
    }

    SenderThread getSenderThread() {
        return senderThread;
    }

    void setSenderThread(SenderThread senderThread) {
        this.senderThread = senderThread;
    }
}