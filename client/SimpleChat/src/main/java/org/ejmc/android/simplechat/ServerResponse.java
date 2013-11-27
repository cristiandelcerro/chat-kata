package org.ejmc.android.simplechat;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: cristianbq
 * Date: 27/11/13
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class ServerResponse {
    private int last_seq;
    private LinkedList<ChatMessage> messages;

    public int getLast_seq() {
        return last_seq;
    }

    public void setLast_seq(int last_seq) {
        this.last_seq = last_seq;
    }

    public LinkedList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<ChatMessage> messages) {
        this.messages = messages;
    }
}
