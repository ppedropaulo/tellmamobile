package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText textUsername;
    private EditText textPassword;
    private EditText textPasswordConfirm;
    private Button mBtnEnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textUsername = findViewById(R.id.editText3);
        textPassword = findViewById(R.id.editText4);
        textPasswordConfirm = findViewById(R.id.editText5);

        final Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
    }


    public void returnLoginActivity(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }



    public void register(){
        String url="http://34.71.71.141/apirest/signup";

        String name = textUsername.getText().toString();
        String pass = textPassword.getText().toString();
        String passC = textPasswordConfirm.getText().toString();

        if (name == null || name.isEmpty() || pass == null || pass.isEmpty() || passC == null || passC.isEmpty()) {
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

                    if(success.equals("true")){
                        Toast.makeText(getApplicationContext(),"Registrado com sucesso",Toast.LENGTH_SHORT).show();
                    }
                    if(success.equals("false")){
                        Toast.makeText(getApplicationContext(),"Erro Inesperado! Crie outro usuário",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException exception) {
                    Log.d("JSONException", "Json exception catched :".concat(exception.getMessage()));
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }



}





