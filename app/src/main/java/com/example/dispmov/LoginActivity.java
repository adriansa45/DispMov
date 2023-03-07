package com.example.dispmov;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button BtnLogin, BtnNewAccount, BtnLenguage;
    private ProgressBar progressbar;
    private Locale locale;
    private Configuration config = new Configuration();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        emailTextView = findViewById(R.id.emailTxt);
        passwordTextView = findViewById(R.id.passwordTxt);
        BtnLogin = findViewById(R.id.LoginBtn);
        BtnNewAccount = findViewById(R.id.CreateBtn);
        BtnLenguage = findViewById(R.id.lenguageBtn);
        progressbar = findViewById(R.id.progressbar);


        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        BtnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,
                        SignupActivity.class);
                startActivity(intent);
            }
        });

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivityDev.class);
            startActivity(intent);
            finish();
        }

        BtnLenguage.setOnClickListener(
                view -> showDialog());
    }

    private void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        //String[] types = getResources().getStringArray(R.array.language);
        String[] types = {"Español","Inglés"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("es");
                        break;
                    case 1:
                        locale = new Locale("en");
                        break;
                }
                config.setLocale(locale);
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(refresh);
                finish();
            }
        });
        b.show();
    }


    private void LoginUser(){
        progressbar.setVisibility(View.VISIBLE);

        String email, password;
        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString().trim();

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
        }

        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task< AuthResult > task)
            {
                if (task.isSuccessful()) {
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.welcome), Toast.LENGTH_LONG).show();
                        db = FirebaseFirestore.getInstance();
                        db.collection("users")
                                .whereEqualTo("email", mAuth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                String rol = document.getData().get("rol").toString();
                                                Intent intent;
                                                switch (rol){
                                                    case "dev":
                                                        intent = new Intent(LoginActivity.this,
                                                                MainActivityDev.class);
                                                        break;
                                                    case "admin":
                                                        intent = new Intent(LoginActivity.this,
                                                                MainActivityAdmin.class);
                                                        break;
                                                    default:
                                                        intent = new Intent(LoginActivity.this,
                                                                MainActivityUser.class);
                                                        break;
                                                }
                                                startActivity(intent);
                                            }
                                        } else {
                                            mAuth.signOut();
                                            Intent intent = new Intent(LoginActivity.this,
                                                    LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.PendingEmail), Toast.LENGTH_LONG).show();
                    }
                    progressbar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error), Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}