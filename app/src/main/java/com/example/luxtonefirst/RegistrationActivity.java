package com.example.luxtonefirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etRegEmail, etRegCountryCode, etRegPassword, etVerificationCode;
    private Button btnRegister, btnVerificationCode;
    private static final String TAG = "Luxtone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();

        etVerificationCode.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);

        btnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeredEmail = etRegEmail.getText().toString();
                String registeredCountryCode = etRegCountryCode.getText().toString();
                getValidationCode(registeredCountryCode,registeredEmail);


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerEmail = etRegEmail.getText().toString();
                String registerCountryCode = etRegCountryCode.getText().toString();
                String registeredPassword = etRegPassword.getText().toString();
                String inputVerificationCode = etVerificationCode.getText().toString();

                TuyaHomeSdk.getUserInstance().registerAccountWithEmail(registerCountryCode, registerEmail, registeredPassword, inputVerificationCode, registerCallback );



            }
        });


    }

    IRegisterCallback registerCallback = new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
            Log.d(TAG, "Successful Registration");
            Toast.makeText(RegistrationActivity.this, "Successful Registration:", Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

        }

        @Override
        public void onError(String code, String error) {
            Log.d(TAG, "Failed Registration" + error);
            Toast.makeText(RegistrationActivity.this, "Failed Registration", Toast.LENGTH_LONG).show();


        }
    };


    IResultCallback validateCallback = new IResultCallback() {
        @Override
        public void onError(String code, String error) {
            Log.d(TAG, "Verification Code Failed with error:" + error);
            Toast.makeText(RegistrationActivity.this, "Failed to send verification Code:", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onSuccess() {
            Toast.makeText(RegistrationActivity.this, "Successfully sent verification code", Toast.LENGTH_LONG).show();
            etVerificationCode.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);

        }
    };

    private void getValidationCode(String countryCode, String email){
        TuyaHomeSdk.getUserInstance().getRegisterEmailValidateCode(countryCode, email, validateCallback);

    }

    private void initViews(){
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegCountryCode = findViewById(R.id.etRegCountryCode);
        etRegPassword = findViewById(R.id.etRegPassword);
        etVerificationCode = findViewById(R.id.etVerificationCode);
        btnRegister = findViewById(R.id.btnUserRegister);
        btnVerificationCode = findViewById(R.id.btnValidate);



    }
}