package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatListActivity extends AppCompatActivity {

    private ChatListAdapter chatListAdapter;
    public static final String TAG = "ROOMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        getChats();

        ArrayList<Chat> initialData = new ArrayList<Chat>();
        chatListAdapter = new ChatListAdapter(this, initialData);

        ListView listView = (ListView) findViewById(R.id.chat_list_recycler_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat) chatListAdapter.getItem(position);
                goToChat(id, chat.getName());
            }
        });
        listView.setAdapter(chatListAdapter);
    }

    private void goToChat(Long id, String name){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", id);
        intent.putExtra("chatName", name);
        startActivity(intent);
    }


    private void handleChatResponse(JSONArray response){
        Gson gson = new Gson();
        Chat[] newChatList = gson.fromJson(response.toString(), Chat[].class);

        if (newChatList == null || newChatList.length == 0) {
            return;
        }

        ArrayList<Chat> newChatArrayList = new ArrayList<Chat>(Arrays.asList(newChatList));
        chatListAdapter.setChatListDataSet(newChatArrayList);
    }

    private void getChatRequest(String userID){
        String url = "http://34.71.71.141/apirest/rooms";
        String urlFormatted = String.format("%1$s?user_id=%2$s", url, userID);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlFormatted, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                handleChatResponse(response);
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

    protected void getChats() {
        UserSession user = UserSession.getInstance();
        String userID = user.getId();

        getChatRequest(userID);
    }

    public void onLogout(View view){
        UserSession.eraseInstance(this.getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onNewChat(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(ChatListActivity.this).create();
        alertDialog.setTitle("Nova conversa");
        // Change setMessage to a personalized form layout
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}