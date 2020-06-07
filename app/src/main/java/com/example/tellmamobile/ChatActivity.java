package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    private EditText editMessage;
    private MessageAdapter adapter;
    private WebSocket ws = null;

    public static final String TAG = "MESSAGES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.setTitleAccordingChat();
        this.getMessages();
        this.openWSConnection();

        editMessage = findViewById(R.id.editMessage);
        ListView messagesListView = findViewById(R.id.listViewMensagens);
        messagesListView.setDivider(null);
        messagesListView.setDividerHeight(0);

        ArrayList<Message> messages = new ArrayList<Message>();
        adapter = new MessageAdapter(this, messages);
        messagesListView.setAdapter(adapter);

        messagesListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messagesListView.setStackFromBottom(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ws != null) {
            ws.disconnect();
            ws = null;
        }
    }

    private ArrayList<Message> sortMessages(ArrayList<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                Date date1 = m1.getDate();
                Date date2 = m2.getDate();


                return date1.compareTo(date2);
            }
        });

        return messages;
    }

    private void handleMessagesResponse(JSONArray response){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Message[] newMessageList = gson.fromJson(response.toString(), Message[].class);

        if (newMessageList == null || newMessageList.length == 0) {
            return;
        }

        ArrayList<Message> newMessageArrayList = new ArrayList<Message>(Arrays.asList(newMessageList));
        newMessageArrayList = sortMessages(newMessageArrayList);
        adapter.setMessages(newMessageArrayList);
    }

    private void getMessagesRequest(Long chatId){
        String urlFormatted = String.format("%1$s%2$s?room_id=%3$s", Constants.API_URL, Constants.MESSAGE_ENDPOINT, chatId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlFormatted, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                handleMessagesResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    private Long getChatId(){
        Intent intent = getIntent();
        return intent.getLongExtra("chatId", 0);
    }

    private void getMessages(){
        Long chatId = getChatId();
        getMessagesRequest(chatId);
    }

    private void setTitleAccordingChat(){
        Intent intent = getIntent();
        String chatName = intent.getStringExtra("chatName");

        this.setTitle(chatName);
    }

    private Long getRandomID(){
        Random rd = new Random();
        return rd.nextLong();
    }

    private Message formatNewMessage(){
        String message = editMessage.getText().toString();
        Long chatId = getChatId();
        assert UserSession.getInstance() != null;
        String username = UserSession.getInstance().getUsername();
        Date date = new Date();
        Long newID = getRandomID();

        Message newMessage = new Message(newID, message, chatId, username, date);
        return newMessage;
    }

    private void closeKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendMessage(View view){
        closeKeyBoard(view);

        Message newMessage = formatNewMessage();
        adapter.addMessage(newMessage);
        editMessage.setText("");
    }

    private void openWSConnection(){
        String urlFormatted = String.format("%1$s/%2$s", Constants.WEBSOCKET_URL, getChatId());
        try {
            ws = new WebSocketFactory().createSocket(urlFormatted);
            ws.connectAsynchronously();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
        }
    }
}