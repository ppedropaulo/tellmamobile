package com.example.tellmamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        this.setTitle("Conversas");
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
            Toast.makeText(getApplicationContext(), "Nenhuma conversa disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Chat> newChatArrayList = new ArrayList<Chat>(Arrays.asList(newChatList));
        chatListAdapter.setChatListDataSet(newChatArrayList);
    }

    private void getChatRequest(String userID){
        String urlFormatted = String.format("%1$s%2$s?user_id=%3$s", Constants.API_URL, Constants.ROOMS_ENDPOINT, userID);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlFormatted, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                handleChatResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
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

    public void refresh(ChatListActivity chatListActivity){
        this.getChats();
    }

    public void onLogout(ChatListActivity chatListActivity){
        UserSession.eraseInstance(this.getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onNewChat(ChatListActivity chatListActivity){
        CreateNewChatDialogFragment dialog = new CreateNewChatDialogFragment();
        dialog.show(getSupportFragmentManager(), "CreateNewChat");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chatlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int val = item.getItemId();

        if(val == R.id.refresh){
            refresh(this);
        }

        if(val == R.id.create_chat){
            onNewChat(this);
        }

        if(val == R.id.exit){
            onLogout(this);
        }

        return super.onOptionsItemSelected(item);
    }

}