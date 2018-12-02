package com.example.rosa.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends BaseActivity {

    private Button seekersButton;
    private Button findersNewButton;
    private Button findersOldButton;
    private Button logoutButton;

    private EditText username;
    private FirebaseAuth mAuth;

    @Override
    int getContentViewId() {
        return R.layout.activity_home2;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Clicked","Home Open");

        /*seekersButton = findViewById(R.id.seekersButton);
        findersNewButton = findViewById(R.id.findersNewButton);
        findersOldButton = findViewById(R.id.findersOldButton);*/
        logoutButton = findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener( v-> {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity (intent);
        });

    }

}
