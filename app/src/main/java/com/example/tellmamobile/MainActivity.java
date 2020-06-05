package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                login();
            }
        });
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onSuccessfulSignIn() {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(){
        String url="http://34.71.71.141/apirest/login";

        String name = textUser.getText().toString();
        String pass = textPass.getText().toString();

        if (name == null || name.isEmpty() || pass == null || pass.isEmpty() ) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap();
        params.put("username",name);
        params.put("password",pass);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String success = response.getString("sucessfull");
                    if(success.equals("false")){
                        Toast.makeText(getApplicationContext(),"Usuário ou senha incorretos",Toast.LENGTH_SHORT).show();
                    }
                    String id = response.getString("id");
                    String username = response.getString("username");
                    UserSession.setInstance(getApplicationContext(),username,id);
                    if(success.equals("true")){
                        UserSession.setInstance(getApplicationContext(),username,id);
                        Toast.makeText(getApplicationContext(),"Bem vindo, "+UserSession.getInstance(getApplicationContext()).getUsername()+"!",Toast.LENGTH_SHORT).show();
                        onSuccessfulSignIn();
                    }
                } catch (JSONException exception) {
                    Log.d("JSONException", "Json exception catched :".concat(exception.getMessage()));
                }
            };

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


