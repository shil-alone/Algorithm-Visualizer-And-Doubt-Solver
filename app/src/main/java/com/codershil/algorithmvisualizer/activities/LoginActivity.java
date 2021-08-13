package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codershil.algorithmvisualizer.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignUp, btnLogin;
    private EditText edtMobileNumber;
    private LottieAnimationView progressBar;
    private CountryCodePicker countryCodePicker;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationId, mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        addClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MOBILE", mobileNumber);
        outState.putString("ID", verificationId);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mobileNumber = savedInstanceState.getString("MOBILE");
        verificationId = savedInstanceState.getString("ID");
    }

    private void initialize() {
        btnSignUp = findViewById(R.id.btnSign);
        btnLogin = findViewById(R.id.btnLog);
        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        progressBar = (LottieAnimationView) findViewById(R.id.progressBar2);
        countryCodePicker = findViewById(R.id.ccp);
        auth = FirebaseAuth.getInstance();

        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(LoginActivity.this, "otp sent successfully", Toast.LENGTH_SHORT).show();
                verificationId = s;
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                intent.putExtra("NUMBER", mobileNumber);
                intent.putExtra("ID", verificationId);
                startActivity(intent);
            }
        };

    }

    private void addClicks() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                mobileNumber = edtMobileNumber.getText().toString().trim();
                if (mobileNumber.length() != 10) {
                    edtMobileNumber.setError("enter valid mobile number");
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                auth.useAppLanguage();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+" + countryCodePicker.getSelectedCountryCode() + mobileNumber)       // Phone number to verify
                                .setTimeout(50L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();

                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });
    }
}