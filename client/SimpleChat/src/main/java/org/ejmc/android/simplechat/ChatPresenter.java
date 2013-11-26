package org.ejmc.android.simplechat;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Timer;

import static org.ejmc.android.simplechat.Message.messageFactory;

public class ChatPresenter extends Timer {

    final static long period = 500;
    final static String server = "172.16.100.47:8080/chat-kata/api/chat";
    private ChatActivity chatActivity;
    //private ReceiverTask receiverTask;

    public ChatPresenter(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
      //  this.receiverTask = new ReceiverTask();
       // schedule(receiverTask, 0, period);
    }

    public void sendMessage (Message message) {
        if(!message.getText().equals(""))
            sendToServer(message);
    }

    private String messageToJson(Message message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        return json;
    }

    private boolean sendToServer(Message message) {
        boolean r = false;
        String json = messageToJson(message);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(server);
            StringEntity stringToSend = new StringEntity(json);

            httppost.setEntity(stringToSend);

            HttpResponse response = httpclient.execute(httppost);
            r = true;
        }

        catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return r;
    }

    public void receiveMessages(LinkedList<Message> messages) {
        chatActivity.addMessages(messages);
    }
}
