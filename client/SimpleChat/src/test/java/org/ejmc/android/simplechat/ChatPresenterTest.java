package org.ejmc.android.simplechat;

import android.content.SharedPreferences;
import android.os.Handler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatPresenterTest {
    private ChatPresenter chatPresenter;
    @Mock
    Vector<ChatMessage> messageList;
    private ServerResponse mockedServerResponse;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        String userName = "test";
        ChatActivity mockedChatActivity = mock(ChatActivity.class);
        Handler mockedChatActivityHandler = mock(Handler.class);

        mockedServerResponse = mock(ServerResponse.class);

        SharedPreferences mockedSettings = mock(SharedPreferences.class);
        when(mockedSettings.getInt("lastSeq", 0)).thenReturn(0);

        when(mockedChatActivity.getSharedPreferences(ChatPresenter.savedProperties, ChatActivity.MODE_PRIVATE))
                .thenReturn(mockedSettings);

        chatPresenter = new ChatPresenter(userName, mockedChatActivity, mockedChatActivityHandler, messageList);

        chatPresenter.setReceiverTimer(mock(ReceiverTimer.class));
        chatPresenter.setSenderThread(mock(SenderThread.class));
    }

//    @Test
//    public void testSendMessage() {
//        LinkedList<ChatMessage> messagesToTest = new LinkedList<>();
//        LinkedList<Integer> expectedResults = new LinkedList<>();
//
//        messagesToTest.add(ChatMessage.messageFactory("Genaro", "asdf")); expectedResults.add(1);
//        messagesToTest.add(ChatMessage.messageFactory("", "asdf")); expectedResults.add(0);
//        messagesToTest.add(ChatMessage.messageFactory("Genaro", "")); expectedResults.add(0);
//
//        Iterator<ChatMessage> it1 = messagesToTest.iterator();
//        Iterator<Integer> it2 = expectedResults.iterator();
//
//        assertMessagesToSendSizeEqualsTo(it1, it2);
//    }
//
//    private void assertMessagesToSendSizeEqualsTo(Iterator<ChatMessage> it1, Iterator<Integer> it2) {
//        for (int i = 0; it1.hasNext() && it2.hasNext(); ++i) {
//            try {
//                ChatMessage currentChatMessage = it1.next();
//                int currentExpectedResult = it2.next();
//
//                verify(chatPresenter.getSenderThread(), times(0)).prepareMessageToSend((ChatMessage)any());
//                chatPresenter.sendMessage(currentChatMessage);
//                verify(chatPresenter.getSenderThread(), times(1)).prepareMessageToSend((ChatMessage)any());
//            }
//
//            catch (AssertionError e) {
//                System.err.println("Error in test " + i);
//                throw e;
//            }
//        }
//    }

    @Test
    public void testSendEmptyMessage() {
        verify(chatPresenter.getSenderThread(), times(0)).prepareMessageToSend((ChatMessage)any());
        chatPresenter.sendMessage(ChatMessage.messageFactory("", ""));
        verify(chatPresenter.getSenderThread(), times(0)).prepareMessageToSend((ChatMessage)any());
    }

    @Test
    public void testSendMessage() {
        verify(chatPresenter.getSenderThread(), times(0)).prepareMessageToSend((ChatMessage)any());
        chatPresenter.sendMessage(ChatMessage.messageFactory("asdf", "asdf"));
        verify(chatPresenter.getSenderThread(), times(1)).prepareMessageToSend((ChatMessage)any());
    }

    @Test
    public void testReceiveMessages() {
        chatPresenter.setStopped(false);
        try {
            chatPresenter.receiveMessages(mockedServerResponse);
        }
        catch(RuntimeException e) {
            System.err.println("El handler da problemas." + e.toString());
            assertEquals("java.lang.RuntimeException: Stub!", e.toString());
        }
        verify(messageList, times(1)).addAll(mockedServerResponse.getMessages());
    }
}
