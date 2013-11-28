package org.ejmc.android.simplechat;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatPresenterTest {
    private ChatPresenter chatPresenter;
    Vector<ChatMessage> messageList;

    @Before
    public void setUp() throws Exception {
        ChatActivity mockedChatActivity = mock(ChatActivity.class);
        Handler mockedChatActivityHandler = mock(Handler.class);
        messageList = new Vector<>();

        SharedPreferences mockedSettings = mock(SharedPreferences.class);
        when(mockedSettings.getInt("lastSeq", 0)).thenReturn(0);

        when(mockedChatActivity.getSharedPreferences(ChatPresenter.savedProperties, ChatActivity.MODE_PRIVATE))
                .thenReturn(mockedSettings);

        chatPresenter = new ChatPresenter(mockedChatActivity, mockedChatActivityHandler, messageList);
    }

    @Test
    public void testSendMessage() {
        LinkedList<ChatMessage> messagesToTest = new LinkedList<>();
        LinkedList<Integer> expectedResults = new LinkedList<>();

        messagesToTest.add(ChatMessage.messageFactory("Genaro", "asdf")); expectedResults.add(1);
        messagesToTest.add(ChatMessage.messageFactory("", "asdf")); expectedResults.add(0);
        messagesToTest.add(ChatMessage.messageFactory("Genaro", "")); expectedResults.add(0);

        Iterator<ChatMessage> it1 = messagesToTest.iterator();
        Iterator<Integer> it2 = expectedResults.iterator();

        assertMessagesToSendSizeEqualsTo(it1, it2);
    }

    private void assertMessagesToSendSizeEqualsTo(Iterator<ChatMessage> it1, Iterator<Integer> it2) {
        for (int i = 0; it1.hasNext() && it2.hasNext(); ++i) {
            try {
                ChatMessage currentChatMessage = it1.next();
                int currentExpectedResult = it2.next();

                chatPresenter.getMessagesToSend().clear();
                assertTrue(chatPresenter.getMessagesToSend().isEmpty());
                chatPresenter.sendMessage(currentChatMessage);
                assertEquals(currentExpectedResult, chatPresenter.getMessagesToSend().size());
            }

            catch (AssertionError e) {
                System.err.println("Error in test " + i);
                throw e;
            }
        }
    }

    @Test
    public void testReceiveMessages() {
        ServerResponse mockedServerResponse = mock(ServerResponse.class);
        chatPresenter.receiveMessages(mockedServerResponse);
        verify(messageList).addAll(mockedServerResponse.getMessages());
    }

    @Test
    public void testFinish() {

    }

    @Test
    public void testGetLastSeq() {

    }

    @Test
    public void testSaveLastSeq() {

    }
}
