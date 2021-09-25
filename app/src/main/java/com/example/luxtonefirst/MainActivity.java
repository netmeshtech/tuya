package com.example.luxtonefirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etCountryCode;
    private Button btnLogin, btnRegister;
    private static final String TAG = "Luxtone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = etCountryCode.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                TuyaHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, loginCallback);

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }
    private ILoginCallback loginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));

        }

        @Override
        public void onError(String code, String error) {
            Log.d(TAG, "Login Failed with error:" + error);
            Toast.makeText(MainActivity.this, "Login Failed with error:" + error, Toast.LENGTH_LONG).show();

        }
    };

    private void initView(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCountryCode = findViewById(R.id.etCountryCode);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);


    }
}