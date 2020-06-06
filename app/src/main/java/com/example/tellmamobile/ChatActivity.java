package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;

    private EditText editMessage;

    private ListView messagesListview;
    private MessageAdapter adapter;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMessage = findViewById(R.id.editMessage);

        messagesListview = findViewById(R.id.listViewMensagens);

        messagesListview.setDivider(null);
        messagesListview.setDividerHeight(0);

        messages = new ArrayList<Message>();

        messages.add(new Message(UserSession.getInstance().getId(),"Seja bem vindo a aula"));
        messages.add(new Message(UserSession.getInstance().getId(),"Lab Prog 3"));

        messages.add(new Message("1","Obrigado"));
        messages.add(new Message(UserSession.getInstance().getId(),"Exemplo de activity para envio de msg"));

        adapter = new MessageAdapter(this, messages);

        messagesListview.setAdapter(adapter);


    }

    public void sendMessage(View view){

        Log.d("TAG","EnviarMensagem");

        String message = editMessage.getText().toString();

        messages.add(new Message(UserSession.getInstance().getId(), message));

        adapter.notifyDataSetChanged();

        editMessage.setText("");

    }
}