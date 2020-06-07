package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText textUser;
    private EditText textPass;
    public static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textUser = findViewById(R.id.editText);
        textPass = findViewById(R.id.editText2);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(v);
            }
        });
    }

    @Override
    protected void onStop () {
        super.onStop();

        if (Requests.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Requests.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(TAG);
        }
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void onSuccessfulSignIn(String name, String id) {
        UserSession.setInstance(this.getApplicationContext(), name, id);

        Intent intent = new Intent(this, ChatListActivity.class);
        Toast.makeText(getApplicationContext(),"Bem vindo, "+UserSession.getInstance().getUsername()+"!",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void handleLoginResponse(JSONObject response){
        try {
            String success = response.getString("sucessfull");

            if(success.equals("false")){
                String error = response.getString("error");
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                return;
            }

            String id = response.getString("id");
            String username = response.getString("username");

            onSuccessfulSignIn(username, id);

        } catch (JSONException exception) {
            Toast.makeText(getApplicationContext(),"Erro na conexão",Toast.LENGTH_SHORT).show();
        }
    }

    private void loginRequest(String name, String pass){
        final Map<String, String> params = new HashMap();
        params.put("username",name);
        params.put("password",pass);
        JSONObject parameters = new JSONObject(params);

        String url = String.format("%1$s%2$s", Constants.API_URL, Constants.LOGIN_ENDPOINT);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleLoginResponse(response);
            };

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Erro na conexão",Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    private void closeKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void login(View view){

        closeKeyBoard(view);

        final String name = textUser.getText().toString();
        final String pass = textPass.getText().toString();

        if (name == null || name.isEmpty() || pass == null || pass.isEmpty() ) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        loginRequest(name, pass);
    }

}


