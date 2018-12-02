package com.example.rosa.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference topRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView profilename = (TextView) findViewById(R.id.nameId);
        TextView SeekersNum = (TextView) findViewById(R.id.SeekersNum);
        TextView FindersNum = (TextView) findViewById(R.id.FindersNum);
        Button logoutButton = (Button) findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener( v-> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity (intent);
        });
        topRef = FirebaseDatabase.getInstance().getReference("Test");


    }
}
