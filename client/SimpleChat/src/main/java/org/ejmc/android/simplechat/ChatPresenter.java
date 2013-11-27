package org.ejmc.android.simplechat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatPresenter {
    final static String server = "http://172.16.100.149:8080/chat-kata/api/chat";

    private ChatActivity chatActivity;
    private ReceiverTimer receiverTimer;
    private SenderTimer senderTimer;
    private ConcurrentLinkedQueue<ChatMessage> messagesToSend;
    private boolean finished;
    private Handler chatActivityHandler;
    private ConcurrentLinkedQueue<ChatMessage> messageList;

    public ChatPresenter(ChatActivity chatActivity, Handler chatActivityHandler, ConcurrentLinkedQueue<ChatMessage> messageList) {
        this.finished = false;
        this.chatActivity = chatActivity;
        this.chatActivityHandler = chatActivityHandler;
        this.messageList = messageList;

        messagesToSend = new ConcurrentLinkedQueue<>();

        senderTimer = new SenderTimer(messagesToSend);
        receiverTimer = new ReceiverTimer(this, messageList);
    }

    public void sendMessage(ChatMessage message) {
        if(!message.getMessage().equals(""))
            messagesToSend.add(message);
    }

    public void receiveMessages(LinkedList<ChatMessage> messagesToPrint) {
        if (!finished) {
            Message msg = Message.obtain();
            Bundle b = new Bundle();
            b.putSerializable("messagesToPrint", messagesToPrint);
            msg.setData(b);
            chatActivityHandler.sendMessage(msg);
        }
    }

    public void finish() {
        finished = true;
        receiverTimer.cancel();
        senderTimer.cancel();
    }
}
