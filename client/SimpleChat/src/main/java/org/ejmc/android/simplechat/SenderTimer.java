package org.ejmc.android.simplechat;

import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SenderTimer extends Timer {
    final static long period = 1500;

    public SenderTimer(ConcurrentLinkedQueue<ChatMessage> messagesToSend) {
        SenderTask senderTask = new SenderTask(messagesToSend);
        schedule(senderTask, 100, period);
    }
}
