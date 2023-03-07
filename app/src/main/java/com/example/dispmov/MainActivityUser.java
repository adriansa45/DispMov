package com.example.dispmov;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dispmov.databinding.ActivityMainUserBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityUser extends AppCompatActivity {

    private ActivityMainUserBinding binding;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dev_accounts, R.id.navigation_dev_trxs, R.id.navigation_user_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_user);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

    }

}