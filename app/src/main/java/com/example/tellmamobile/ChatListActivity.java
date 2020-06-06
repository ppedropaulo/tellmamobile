package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        listView.setAdapter(chatListAdapter);
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
        UserSession user = UserSession.getInstance(this);
        String userID = user.getId();

        getChatRequest(userID);
    }

    public void onLogout(View view){
        UserSession.eraseInstance(this.getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}