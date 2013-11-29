package org.ejmc.android.simplechat;

import java.util.Timer;

public class ReceiverTimer extends Timer {
    final static long period = 1500;

    public ReceiverTimer(ChatPresenter chatPresenter) {
        ReceiverTask receiverTask = new ReceiverTask(chatPresenter);
        schedule(receiverTask, 100, period);
    }
}
