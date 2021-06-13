package com.codershil.algorithmvisualizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codershil.algorithmvisualizer.R;
import com.codershil.algorithmvisualizer.daos.UserDao;
import com.codershil.algorithmvisualizer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    Button btnVerifyOtp;
    EditText edtOtp;
    TextView txtTime, txtResendOtp, textView;
    LottieAnimationView progressBar;
    CountDownTimer timer;
    Intent intent;
    String verificationId, mobileNumber, userName, userEmail;
    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Log.i("log", "on create ran");
        initialize();
        addClicks();
        intent = getIntent();
        verificationId = intent.getStringExtra("ID");
        mobileNumber = intent.getStringExtra("NUMBER");
        userEmail = intent.getStringExtra("EMAIL");
        userName = intent.getStringExtra("NAME");
        timer.start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NUMBER", mobileNumber);
        outState.putString("EMAIL", userEmail);
        outState.putString("NAME", userName);
        outState.putString("ID", verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userName = savedInstanceState.getString("NAME");
        mobileNumber = savedInstanceState.getString("NUMBER");
        userEmail = savedInstanceState.getString("EMAIL");
        verificationId = savedInstanceState.getString("ID");
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        btnVerifyOtp.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        edtOtp = findViewById(R.id.edtOtp);
        txtTime = findViewById(R.id.txtTime);
        textView = findViewById(R.id.textView);
        txtResendOtp = findViewById(R.id.txtResendOtp);
        progressBar = (LottieAnimationView) findViewById(R.id.progressBar);


        // initializing firebase objects
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // changing the color of status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }

        // initializing timer for otp counter
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTime.setText("00:" + millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                timer.cancel();
                txtResendOtp.setVisibility(View.VISIBLE);
            }
        };
    }

    private void addClicks() {
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOtp.getText().toString().isEmpty()) {
                    edtOtp.setError("please enter otp");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (verificationId != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, edtOtp.getText().toString().trim());
                    auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (getIntent().getIntExtra("SIGN", 0) == 1) {
                                    User user = new User(userName, userEmail, mobileNumber, auth.getCurrentUser().getUid());
                                    UserDao dao = new UserDao(OtpActivity.this);
                                    dao.addUser(user);
                                }

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(OtpActivity.this, "otp verified successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OtpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(OtpActivity.this, "failed to verify otp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OtpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnVerifyOtp.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });

        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        txtResendOtp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        txtResendOtp.setVisibility(View.VISIBLE);
                        Toast.makeText(OtpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        Toast.makeText(OtpActivity.this, "otp resent", Toast.LENGTH_SHORT).show();
                    }
                };

                progressBar.setVisibility(View.VISIBLE);
                txtResendOtp.setVisibility(View.GONE);
                auth.useAppLanguage();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91" + mobileNumber)
                                .setTimeout(50L, TimeUnit.SECONDS)
                                .setActivity(OtpActivity.this)
                                .setCallbacks(mCallbacks)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }

}