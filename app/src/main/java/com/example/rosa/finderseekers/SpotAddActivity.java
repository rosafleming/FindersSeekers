package com.example.rosa.finderseekers;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SpotAddActivity extends AppCompatActivity {

    private Spinner state;
    private Button post;
    private EditText name;
    private EditText description;
    private EditText directions;
    private EditText city;

    DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_add);
        //Log.i("Clicked","Finders Open");
        city = findViewById(R.id.city_input);

        //addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

    }

    // add items into spinner dynamically
   /* public void addItemsOnSpinner2() {

        //city = (Spinner) findViewById(R.id.city_spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Grand Rapids");
        list.add("Detroit");
        list.add("Flint");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SpotAddActivity.this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        city.setAdapter(dataAdapter);
    }*/

    public void addListenerOnSpinnerItemSelection() {
        state = (Spinner) findViewById(R.id.state_spinner);
        state.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

       // state = (Spinner) findViewById(R.id.state_spinner);
       // city = (Spinner) findViewById(R.id.city_spinner);
        post = (Button) findViewById(R.id.post_button);
        name = (EditText) findViewById(R.id.name_text);
        description = (EditText) findViewById(R.id.description_text);
        directions = (EditText) findViewById(R.id.direction_text);


        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               /* Toast.makeText(SpotAddActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();*/

               SpotContent.SpotItem output = new SpotContent.SpotItem(
                      name.getText().toString(),
                       String.valueOf(state.getSelectedItem()),
                       city.getText().toString(),
                       description.getText().toString(),
                       directions.getText().toString(),
                       MainActivity.EMAIL
               );

                topRef = FirebaseDatabase.getInstance().getReference("Spots");
                topRef.push().setValue(output);

                Intent intent = new Intent(SpotAddActivity.this, FindersActivity.class);
                startActivity (intent);
            }

        });
    }

}
