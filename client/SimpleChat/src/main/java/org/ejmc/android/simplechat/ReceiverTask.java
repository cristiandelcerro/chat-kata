package org.ejmc.android.simplechat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReceiverTask extends TimerTask {
    private ChatPresenter chatPresenter;
    private ConcurrentLinkedQueue<ChatMessage> messageList;

    public ReceiverTask(ChatPresenter chatPresenter, ConcurrentLinkedQueue<ChatMessage> messageList) {
        this.chatPresenter = chatPresenter;
        this.messageList = messageList;
    }

    @Override
    public void run() {
        LinkedList<ChatMessage> messagesReceived = new LinkedList<>();

        try {
            String resp = getResp();
            ServerResponse serverResponse = getServerResponse(resp);

            if (serverResponse.getMessages().isEmpty()) return;

            // TODO: Modificar lista de mensajes desde el presenter.
            messagesReceived = getMessagesReceived(messagesInJSONReceived);


        } catch (JSONException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        } catch (ClientProtocolException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        } catch (IOException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        }

        chatPresenter.receiveMessages(messagesReceived);
    }

    private LinkedList<ChatMessage> getMessagesReceived(LinkedList<JSONObject> messagesInJSONReceived) throws JSONException {
        LinkedList<ChatMessage> messagesReceived = new LinkedList<>();

        for (JSONObject j: messagesInJSONReceived) {
            ChatMessage msg = ChatMessage.messageFactory(j);
            messagesReceived.add(msg);
        }

        return messagesReceived;
    }

    public String getResp() throws JSONException, IOException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet get = new HttpGet(ChatPresenter.server);
        get.setHeader("content-type", "application/json");
        get.getParams().setIntParameter("seq", 0);

        HttpResponse resp = httpClient.execute(get);

        return EntityUtils.toString(resp.getEntity());
    }

    public ServerResponse getServerResponse(String resp) throws JSONException {
        Gson gson = new Gson();

        Type serverResponseType = new TypeToken<ServerResponse>(){}.getType();

        return gson.fromJson(resp, serverResponseType);
    }
}
