package com.example.rosa.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FindersActivity extends BaseActivity {


    private Button post;

    DatabaseReference topRef;

    @Override
    int getContentViewId() {
        return R.layout.activity_finders2;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Clicked","Finders Open");


        addListenerOnButton();

    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        post = (Button) findViewById(R.id.postbutton);

        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindersActivity.this, SpotAddActivity.class);
                startActivity (intent);
            }

        });
    }

}
