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
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.bg_group_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.setTitle(chatName);
    }

    private void closeKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isTextBoxNotFilled(){
        String message = editMessage.getText().toString();
        return message == null || message.isEmpty();
    }

    public void sendMessage(View view){
        if(isTextBoxNotFilled()){
            Toast.makeText(getApplicationContext(), "Escreva uma mensagem", Toast.LENGTH_SHORT).show();
            return;
        }

        closeKeyBoard(view);

        String message = editMessage.getText().toString();
        String userId = UserSession.getInstance().getId();

        JSONObject json = new JSONObject();
        try {
            json.put("text", message);
            json.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ws.isOpen()) {
            ws.sendText(json.toString());
        }

        editMessage.setText("");
    }

    private void openWSConnection(){
        String urlFormatted = String.format("%1$s/%2$s", Constants.WEBSOCKET_URL, getChatId());
        try {
            ws = new WebSocketFactory().createSocket(urlFormatted);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextFrame(WebSocket websocket, WebSocketFrame frame){
                    String frameText = frame.getPayloadText();

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Message[] newMessageList = gson.fromJson(frameText.toString(), Message[].class);
                    Message newMessage =  newMessageList[0];

                    adapter.addMessage(newMessage);
                }

            });

            ws.connectAsynchronously();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
        }
    }
}