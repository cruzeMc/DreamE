package com.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.events.authentication.VerifyResponse;
import com.events.custom.CustomActivity;
import com.events.model.Login;
import com.events.services.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.events.sync.ServerCommunicator;

public class LoginActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Get email
                EditText email = (EditText) findViewById(R.id.email);
                String emailText = email.getText().toString();

                // Get password
                EditText password = (EditText) findViewById(R.id.password);
                String passwordText = password.getText().toString();

                Login user = new Login(emailText, passwordText);

                TransmitData(user);
            }
        });
    }

    private void TransmitData(Login user) {
        APIService service = ServerCommunicator.getServerCommunicator().create(APIService.class);
        Call<VerifyResponse> call = service.postLogin(user);

        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.isSuccessful() && response.body() != null  ) {
                    if (response.body().getSuccess()) {
                        Context context = getApplicationContext();
                        CharSequence text = "Login was successful";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // transition to MainActivity upon login
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        VerifyResponse bool = response.body();
                        Log.d("Login", ""+bool.getSuccess());
                        Context context = getApplicationContext();
                        CharSequence text = "Email or password is incorrect";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = "Network error";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
