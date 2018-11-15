package com.example.rosa.finderseekers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Home {

    private Button seekersButton;
    private Button findersNewButton;
    private Button findersOldButton;
    private Button logoutButton;

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        seekersButton = findViewById(R.id.seekersButton);
        findersNewButton = findViewById(R.id.findersNewButton);
        findersOldButton = findViewById(R.id.findersOldButton);
        logoutButton = findViewById(R.id.logoutButton);

        seekersButton.setOnListener(v ->{

        });
    }

    @Override
    public View onCreateView(){

    }
}
