package com.example.dispmov;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView,ConfirmPasswordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private Spinner mySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();


        mySpinner = (Spinner) findViewById(R.id.spinner);
        emailTextView = findViewById(R.id.emailTxt);
        passwordTextView = findViewById(R.id.passwordTxt);
        ConfirmPasswordTextView = findViewById(R.id.ConfirmPasswordTxt);
        Btn = findViewById(R.id.LoginBtn);
        progressbar = findViewById(R.id.progressbar);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });

    }

    private void registerNewUser()
    {

        progressbar.setVisibility(View.VISIBLE);

        String email, password, second_password;
        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString().trim();
        second_password = ConfirmPasswordTextView.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.EmpyEmail),
                            Toast.LENGTH_LONG)
                    .show();
            progressbar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.EmpyPassword),
                            Toast.LENGTH_LONG)
                    .show();
            progressbar.setVisibility(View.GONE);
            return;
        }else{
            if (!password.equals(second_password)){
                Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.PasswordMatching),
                                Toast.LENGTH_LONG)
                        .show();
                progressbar.setVisibility(View.GONE);
                return;
            }
        }

        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            String rol = mySpinner.getSelectedItem().toString();
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("rol", rol);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.Error),Toast.LENGTH_LONG).show();
                                        }
                                    });

                            mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),
                                                        getResources().getString(R.string.EmailSended),
                                                        Toast.LENGTH_LONG)
                                                .show();





                                        Intent intent
                                                = new Intent(SignupActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                    }
                                    progressbar.setVisibility(View.GONE);
                                });


                        }
                        else {

                            Toast.makeText(
                                            getApplicationContext(),
                                            getResources().getString(R.string.Error),
                                            Toast.LENGTH_LONG)
                                    .show();

                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}