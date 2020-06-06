package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText textUsername;
    private EditText textPassword;
    private EditText textPasswordConfirm;
    public static final String TAG = "REGISTER";


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

    private void handleRegisterResponse(JSONObject response){
        try {
            String success = response.getString("sucessfull");

            if(success.equals("true")){
                Toast.makeText(getApplicationContext(),"Registrado com sucesso",Toast.LENGTH_SHORT).show();
            }

            if(success.equals("false")){
                String error = response.getString("error");
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException exception) {
            Log.d("JSONException", "Json exception catched :".concat(exception.getMessage()));
        }
    }

    private void  registerRequest(String username, String password){
        String url="http://34.71.71.141/apirest/signup";

        Map<String, String> params = new HashMap();
        params.put("username",username);
        params.put("password",password);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleRegisterResponse(response);
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

    private boolean isFormFulled(String name, String pass, String passConf){
        return name == null || name.isEmpty() || pass == null || pass.isEmpty() || passConf == null || passConf.isEmpty();
    }

    private boolean isPasswordConfirmed(String password, String passwordConf){
        return password.equals(passwordConf);
    }

    private boolean isFormValid(String name, String pass, String passConf){

        if (isFormFulled(name, pass, passConf)) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isPasswordConfirmed(pass, passConf)){
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void register(){

        String name = textUsername.getText().toString();
        String pass = textPassword.getText().toString();
        String passC = textPasswordConfirm.getText().toString();

        if (isFormValid(name, pass, passC)) {
            return;
        }

        registerRequest(name, pass);
    }
}





