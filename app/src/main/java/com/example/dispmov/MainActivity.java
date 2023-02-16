package com.example.dispmov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button BtnLogOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnLogOut = findViewById(R.id.LogOutBtn);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (!mAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(),
                            "Usuario sin autenticar.",
                            Toast.LENGTH_LONG)
                    .show();
            mAuth.signOut();
        }

        BtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                                "Bye!!",
                                Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}