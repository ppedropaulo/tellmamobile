package com.example.tellmamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText textUsername;
    private EditText textPassword;
    private EditText textPasswordConfirm;
    private LoadingDialog loading;
    public static final String TAG = "REGISTER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        loading = new LoadingDialog(this);

        textUsername = findViewById(R.id.editText3);
        textPassword = findViewById(R.id.editText4);
        textPasswordConfirm = findViewById(R.id.editText5);

        final Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register(v);
            }
        });
    }

    private void handleRegisterResponse(JSONObject response){
        try {
            String success = response.getString("sucessfull");

            if(success.equals("true")){
                Toast.makeText(getApplicationContext(),"Registrado com sucesso",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = response.getString("error");
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException exception) {
            Toast.makeText(getApplicationContext(),"Erro de conexão",Toast.LENGTH_SHORT).show();
        }
    }

    private void  registerRequest(String username, String password){
        loading.startLoadingDialog();
        Map<String, String> params = new HashMap();
        params.put("username",username);
        params.put("password",password);
        JSONObject parameters = new JSONObject(params);

        String url = String.format("%1$s%2$s", Constants.API_URL, Constants.REGISTER_ENDPOINT);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismissDialog();
                handleRegisterResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismissDialog();
                Toast.makeText(getApplicationContext(),"Erro de conexão",Toast.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        Requests.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    private boolean isFormNotFilled(String name, String pass, String passConf){
        return name == null || name.isEmpty() || pass == null || pass.isEmpty() || passConf == null || passConf.isEmpty();
    }

    private boolean isPasswordConfirmed(String password, String passwordConf){
        return password.equals(passwordConf);
    }

    private boolean isFormValid(String name, String pass, String passConf){
        if (isFormNotFilled(name, pass, passConf)) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isPasswordConfirmed(pass, passConf)){
            Toast.makeText(this, "Senhas diferentes", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void closeKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void register(View view){

        closeKeyBoard(view);

        String name = textUsername.getText().toString();
        String pass = textPassword.getText().toString();
        String passC = textPasswordConfirm.getText().toString();

        if (!isFormValid(name, pass, passC)) {
            return;
        }

        registerRequest(name, pass);
    }
}





