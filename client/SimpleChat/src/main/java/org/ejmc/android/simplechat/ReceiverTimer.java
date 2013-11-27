package org.ejmc.android.simplechat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReceiverTimer extends Timer {
    final static long period = 1500;

    public ReceiverTimer(ChatPresenter chatPresenter, ConcurrentLinkedQueue<ChatMessage> messageList) {
        ReceiverTask receiverTask = new ReceiverTask(chatPresenter, messageList);
        schedule(receiverTask, 100, period);
    }
}
