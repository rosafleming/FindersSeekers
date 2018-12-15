package com.gonzalez.fs.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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


        logoutButton = findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener( v-> {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity (intent);
        });

    }

}
