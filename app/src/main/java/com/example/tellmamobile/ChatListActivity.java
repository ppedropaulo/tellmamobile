package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {

    private ArrayList<Chat> chatListDataSet;
    private ChatListAdapter chatListAdapter;
    public static final String TAG = "ROOMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        getChats();

        ListView listView = (ListView) findViewById(R.id.chat_list_recycler_view);

        chatListAdapter = new ChatListAdapter(this, chatListDataSet);
        listView.setAdapter(chatListAdapter);
    }

    protected void getChats(){
        String url="http://34.71.71.141/apirest/rooms";

        UserSession user = UserSession.getInstance(this);
        String userID = user.getId();

        String urlFormatted = String.format("%1$s?user_id=%2$s",url,userID);

        final ChatListActivity act = this;


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlFormatted,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Chat[] newChatList = gson.fromJson(response.toString(), Chat[].class);
                if (newChatList == null || newChatList.length == 0){
                    return;
                }

                ArrayList<Chat> newChatArrayList = new ArrayList<Chat>(Arrays.asList(newChatList));
                act.chatListAdapter.setChatListDataSet(newChatArrayList);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }
}