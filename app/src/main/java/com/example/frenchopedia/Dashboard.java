package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNav;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bottomNav=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener  navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.fragment_home:
                            navController=Navigation.findNavController(Dashboard.this,R.id.nav_dashboard);
                            navController.navigate(R.id.homeFragment);
                            break;
                        case R.id.fragment_extra:
                            navController=Navigation.findNavController(Dashboard.this,R.id.nav_dashboard);
                            navController.navigate(R.id.extraFragment);
                            break;
                        case R.id.fragment_course:
                            navController=Navigation.findNavController(Dashboard.this,R.id.nav_dashboard);
                            navController.navigate(R.id.courseFragment);
                            break;
                        case R.id.fragment_profile:
                            navController=Navigation.findNavController(Dashboard.this,R.id.nav_dashboard);
                            navController.navigate(R.id.profileFragment);
                            break;
                    }
                    return true;
                }
            };
}
