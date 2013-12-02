package org.ejmc.android.simplechat;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;

public class SenderThread extends Thread {
    private LinkedList<ChatMessage> messagesToSend;
    private boolean finished;

    public SenderThread() {
        this.messagesToSend = new LinkedList<>();
        this.finished = false;
    }

    private String messageToJson(ChatMessage message) {
        Gson gson = new Gson();
        return gson.toJson(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                waitEvent();

                if (isFinished())
                    break;

                while (!isFinished() && thereAreMessagesToSend())
                    sendOneMessage();
            }
        }

        catch (InterruptedException e) {
            System.err.println("El hilo enviador de mensajes ha terminado mal.");
            System.err.println(e.toString());
            finish();
        }
    }

    private synchronized boolean thereAreMessagesToSend() {
        return !messagesToSend.isEmpty();
    }

    private synchronized void waitEvent() throws InterruptedException {
        while (!finished && messagesToSend.isEmpty())
            wait();
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized void finish() {
        finished = true;
        notifyAll();
    }

    void sendOneMessage() {
        String json;

        synchronized (this) {
            json = messageToJson(messagesToSend.peek());
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(ChatPresenter.server);
        post.setHeader("content-type", "application/json");

        try {
            StringEntity stringToSend = new StringEntity(json);

            post.setEntity(stringToSend);

            HttpResponse response = httpclient.execute(post);
            String respStr = EntityUtils.toString(response.getEntity());
            if(response.getStatusLine().getStatusCode() == 200) {
                synchronized (this) {
                    messagesToSend.poll();
                }
            }

            else
                System.err.println("No se ha podido enviar el mensaje, reintentando en la próxima iteración.");
        } catch (IOException e) {
            System.err.println("No se ha podido enviar el mensaje, reintentando en la próxima iteración. " + e.toString());
        }
    }

    public synchronized void prepareMessageToSend(ChatMessage m) {
        messagesToSend.add(m);
        notifyAll();
    }
}
