package org.ejmc.android.simplechat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimerTask;

public class ReceiverTask extends TimerTask {
    private ChatPresenter chatPresenter;

    public ReceiverTask(ChatPresenter chatPresenter) {
        this.chatPresenter = chatPresenter;
    }

    @Override
    public void run() {
        try {
            String resp = getResp();
            ServerResponse serverResponse = getServerResponse(resp);

            if (serverResponse.getMessages().isEmpty()) return;

            chatPresenter.receiveMessages(serverResponse);

        } catch (JSONException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        } catch (ClientProtocolException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        } catch (IOException e) {
            System.err.println("No se han podido recibir los mensajes, reintentando en la próxima iteración. " + e.toString());
        }

    }

    private String getResp() throws JSONException, IOException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet get = new HttpGet(ChatPresenter.server + "?next_seq=" + chatPresenter.getLastSeq());
        get.setHeader("content-type", "application/json");

        HttpResponse resp = httpClient.execute(get);

        return EntityUtils.toString(resp.getEntity());
    }

    private ServerResponse getServerResponse(String resp) throws JSONException {
        Gson gson = new Gson();

        Type serverResponseType = new TypeToken<ServerResponse>(){}.getType();

        return gson.fromJson(resp, serverResponseType);
    }
}
