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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codershil.algorithmvisualizer.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSignUp, btnLogin;
    private EditText edtName, edtMobile, edtEmail;
    private LottieAnimationView progressBar;
    private CountryCodePicker countryCodePicker;
    String userName, userMobile, userEmail, verificationId;
    Intent intent;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initialize();
        signUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSignUp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MOBILE", userMobile);
        outState.putString("ID", verificationId);
        outState.putString("NAME", userName);
        outState.putString("EMAIL", userEmail);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userMobile = savedInstanceState.getString("MOBILE");
        verificationId = savedInstanceState.getString("ID");
        userName = savedInstanceState.getString("NAME");
        userEmail = savedInstanceState.getString("EMAIL");
    }

    private void initialize() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        edtName = findViewById(R.id.edtName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        progressBar = (LottieAnimationView) findViewById(R.id.progressBar3);
        countryCodePicker = findViewById(R.id.ccp1);

        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }
        auth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                Toast.makeText(SignUpActivity.this, "failed to sent otp", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(SignUpActivity.this, "otp send successfully", Toast.LENGTH_SHORT).show();
                intent = new Intent(SignUpActivity.this, OtpActivity.class);
                verificationId = s;
                intent.putExtra("ID", verificationId);
                intent.putExtra("NUMBER", userMobile);
                intent.putExtra("EMAIL", userEmail);
                intent.putExtra("NAME", userName);
                intent.putExtra("SIGN", 1);
                finish();
                startActivity(intent);
            }

        };
    }

    private void signUp() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.GONE);
                userName = edtName.getText().toString().trim();
                userMobile = edtMobile.getText().toString().trim();
                userEmail = edtEmail.getText().toString().trim();

                if (userName.isEmpty()) {
                    edtName.setError("please enter name");
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    return;
                }
                if (userMobile.length() != 10) {
                    edtMobile.setError("please enter a valid phone number");
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    return;
                }
                if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    edtEmail.setError("please enter a valid email");
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    return;
                }
                auth.useAppLanguage();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+"+countryCodePicker.getSelectedCountryCode() + userMobile)       // Phone number to verify
                                .setTimeout(50L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(SignUpActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}