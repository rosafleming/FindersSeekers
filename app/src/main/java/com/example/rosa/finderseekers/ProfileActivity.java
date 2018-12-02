package com.example.rosa.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView profilename = (TextView) findViewById(R.id.nameId);
        TextView SeekersNum = (TextView) findViewById(R.id.SeekersNum);
        TextView FindersNum = (TextView) findViewById(R.id.FindersNum);
        Button logoutButton = (Button) findViewById(R.id.logout);


        logoutButton.setOnClickListener( v-> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity (intent);
        });

        

    }
}
