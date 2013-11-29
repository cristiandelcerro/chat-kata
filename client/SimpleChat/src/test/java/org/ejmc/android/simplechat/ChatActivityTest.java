package org.ejmc.android.simplechat;

import android.widget.Button;
import android.widget.EditText;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class ChatActivityTest {

    private ChatActivity chatActivity;
    Button sendButton;
    EditText textViewToSend;

    @Before
    public void setUp() {
        chatActivity = new ChatActivity();
        chatActivity.onCreate(null);

        sendButton = (Button)chatActivity.findViewById(R.id.sendButton);
        textViewToSend = (EditText)chatActivity.findViewById(R.id.textToSend);
    }

    @Test
    public void testSendMessage() {
        ChatPresenter mockedChatPresenter = mock(ChatPresenter.class);
        String userName = "TestUser";
        String messageText = "Mensaje a enviar";
        chatActivity.setChatPresenter(mockedChatPresenter);
        chatActivity.setUserNameChat(userName);

        textViewToSend.setText(messageText);
        sendButton.performClick();

        verify(mockedChatPresenter, times(1)).sendMessage((ChatMessage)any());
        assertEquals("", textViewToSend.getText().toString());
    }
}