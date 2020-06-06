package com.example.tellmamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNewChatDialogFragment extends DialogFragment {
    String TAG = "NEW_CHAT_MODAL";
    private EditText editChatName;
    private EditText editChatUsers;

    private void onSuccessfulNewChat(Activity act){
        act.recreate();
    }

    private void handleResponse(JSONObject response, Activity act){
        try {
            String success = response.getString("sucessfull");

            if(success.equals("false")){
                Toast.makeText(getContext(),"Erro ao criar sala",Toast.LENGTH_SHORT).show();
                return;
            }

            onSuccessfulNewChat(act);

        } catch (JSONException exception) {
            Toast.makeText(getContext(),"Erro na conexão",Toast.LENGTH_SHORT).show();
        }
    }
    private void NewChatRequest(String name, String users_text, final Activity act){
        String url = "http://34.71.71.141/apirest/rooms";

        List<String> user_list = new ArrayList<String>(Arrays.asList(users_text.split("\n")));
        JSONArray users = new JSONArray();

        for(String user: user_list){
            users.put(user);
        }

        String own_user = UserSession.getInstance().getUsername();
        users.put(own_user);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("users", users);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleResponse(response, act);
            };

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", String.valueOf(error));
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getContext()).addToRequestQueue(request);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_chat_dialog, null);
        builder.setTitle("Nova conversa");
        builder.setView(view);
        builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = editChatName.getText().toString();
                        String users = editChatUsers.getText().toString();
                        NewChatRequest(name, users, getActivity());

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        editChatName = view.findViewById(R.id.edit_chat_name);
        editChatUsers = view.findViewById(R.id.edit_chat_users);


        // Create the AlertDialog object and return it
        return builder.create();
    }
}