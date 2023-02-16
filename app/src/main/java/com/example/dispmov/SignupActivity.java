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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView,ConfirmPasswordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

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
                            "Ingresa un correo!!",
                            Toast.LENGTH_LONG)
                    .show();
            progressbar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Ingresa una contreseña!!",
                            Toast.LENGTH_LONG)
                    .show();
            progressbar.setVisibility(View.GONE);
            return;
        }else{
            if (!password.equals(second_password)){
                Toast.makeText(getApplicationContext(),
                                "Las contraseñas no coinciden.",
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

                            mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),
                                                        "Correo de verificación enviado",
                                                        Toast.LENGTH_LONG)
                                                .show();

                                        progressbar.setVisibility(View.GONE);

                                        Intent intent
                                                = new Intent(SignupActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(
                                                        getApplicationContext(),
                                                        "Error al registrarse. Intente de nuevo.",
                                                        Toast.LENGTH_LONG)
                                                .show();

                                        progressbar.setVisibility(View.GONE);
                                    }
                                });


                        }
                        else {

                            Toast.makeText(
                                            getApplicationContext(),
                                            "Error al registrarse. Intente de nuevo.",
                                            Toast.LENGTH_LONG)
                                    .show();

                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}