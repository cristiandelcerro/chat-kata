package org.ejmc.android.simplechat;

import android.widget.Button;
import android.widget.EditText;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class ChatActivityTest {

    private ChatActivity chatActivity;
    Button sendButton;
    EditText textToSend;

    @Before
    public void setUp() throws Exception {
        chatActivity = new ChatActivity();
        chatActivity.onCreate(null);

        sendButton = (Button)chatActivity.findViewById(R.id.sendButton);
        textToSend = (EditText)chatActivity.findViewById(R.id.textToSend);
    }

}
