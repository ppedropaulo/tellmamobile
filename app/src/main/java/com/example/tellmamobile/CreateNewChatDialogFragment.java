package com.example.tellmamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateNewChatDialogFragment extends DialogFragment {
    String TAG = "NEW_CHAT_MODAL";
    private EditText editChatName;
    private EditText editChatUsers;
    private Activity mActivity;
    private LoadingDialog loading;

    private void onSuccessfulNewChat(){
        ((ChatListActivity) mActivity).getChats();
    }

    private void handleResponse(JSONObject response){
        try {
            String success = response.getString("sucessfull");

            if(success.equals("false")){
                Toast.makeText(getContext(),"Erro ao criar conversa",Toast.LENGTH_SHORT).show();
                return;
            }

            onSuccessfulNewChat();

        } catch (JSONException exception) {
            Toast.makeText(getContext(),"Erro ao criar conversa",Toast.LENGTH_SHORT).show();
        }
    }

    private void newChatRequest(JSONObject json){
        loading.startLoadingDialog();
        String url = String.format("%1$s%2$s", Constants.API_URL, Constants.ROOMS_ENDPOINT);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismissDialog();
                handleResponse(response);
            };

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismissDialog();
                Toast.makeText(getContext(),"Erro na conexão",Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getContext()).addToRequestQueue(request);
    }

    private JSONObject getNewChatJSON(String name, String users_text){
        List<String> user_list = new ArrayList<String>(Arrays.asList(users_text.split("\n")));

        JSONArray users = new JSONArray();
        for(String user: user_list){
            users.put(user);
        }

        assert UserSession.getInstance() != null;
        String own_user = UserSession.getInstance().getUsername();
        users.put(own_user);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("users", users);
        } catch (JSONException e) {
            Toast.makeText(mActivity, "Erro ao criar a conversa", Toast.LENGTH_SHORT).show();
        }

        return json;
    }

    private boolean isFormNotFilled(String name, String users){
        return name == null || name.isEmpty() || users == null || users.isEmpty();
    }

    private boolean isFormValid(String name, String users){
        if (isFormNotFilled(name, users)) {
            Toast.makeText(mActivity, "Erro: Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void closeKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void createNewChat(View view){
        closeKeyBoard(view);

        String name = editChatName.getText().toString();
        String users = editChatUsers.getText().toString();

        if (!isFormValid(name, users)) {
            return;
        }

        JSONObject json = getNewChatJSON(name, users);
        newChatRequest(json);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.new_chat_dialog, null);
        builder.setTitle("Nova conversa");
        builder.setView(view);

        builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity = getActivity();
                        loading = new LoadingDialog(mActivity);
                        createNewChat(view);
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