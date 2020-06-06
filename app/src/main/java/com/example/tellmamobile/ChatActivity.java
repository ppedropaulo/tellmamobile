package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText editMessage;

    private ListView messagesListview;
    private MessageAdapter adapter;
    private ArrayList<Message> messages;
    public static final String TAG = "MESSAGES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.setTitleAccordingChat();
        this.getMessages();

        editMessage = findViewById(R.id.editMessage);
        messagesListview = findViewById(R.id.listViewMensagens);
        messagesListview.setDivider(null);
        messagesListview.setDividerHeight(0);

        messages = new ArrayList<Message>();
        // messages.add(new Message(UserSession.getInstance().getId(),"Seja bem vindo a aula", chat, username, date));
        // messages.add(new Message(UserSession.getInstance().getId(),"Lab Prog 3", chat, username, date));
        // messages.add(new Message("1","Obrigado", chat, username, date));
        // messages.add(new Message(UserSession.getInstance().getId(),"Exemplo de activity para envio de msg", chat, username, date));

        adapter = new MessageAdapter(this, messages);
        messagesListview.setAdapter(adapter);
    }

    private void handleMessagesResponse(JSONArray response){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Message[] newChatList = gson.fromJson(response.toString(), Message[].class);
        System.out.println(newChatList);
    }

    private void getMessagesRequest(Long chatId){
        String url = "http://34.71.71.141/apirest/messages";
        String urlFormatted = String.format("%1$s?room_id=%2$s", url, chatId);

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

    private void getMessages(){
        Intent intent = getIntent();
        Long chatId = intent.getLongExtra("chatId", 0);

        getMessagesRequest(chatId);
    }

    private void setTitleAccordingChat(){
        Intent intent = getIntent();
        String chatName = intent.getStringExtra("chatName");

        this.setTitle(chatName);
    }

    public void sendMessage(View view){

        Log.d("TAG","EnviarMensagem");

        String message = editMessage.getText().toString();

        // messages.add(new Message(UserSession.getInstance().getId(), message, chat, username, date));

        adapter.notifyDataSetChanged();

        editMessage.setText("");

    }
}