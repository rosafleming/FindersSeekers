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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindersActivity extends BaseActivity {


    private Button post;

    DatabaseReference topRef;
    DatabaseReference spots;

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

        DatabaseReference topRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference spots = topRef.child("Spots");


        spots.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SpotContent.ITEMS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getValue(SpotContent.SpotItem.class).getUsername().equals(MainActivity.EMAIL)) {
                        SpotContent.SpotItem item = new SpotContent.SpotItem(
                                ds.getValue(SpotContent.SpotItem.class).getName(),
                                ds.getValue(SpotContent.SpotItem.class).getState(),
                                ds.getValue(SpotContent.SpotItem.class).getCity(),
                                ds.getValue(SpotContent.SpotItem.class).getDescription(),
                                ds.getValue(SpotContent.SpotItem.class).getDirections(),
                                ds.getValue(SpotContent.SpotItem.class).getUsername()
                        );

                        SpotContent.addItem(item);
                    }
                }
                String[] SpotList = new String[SpotContent.ITEMS.size()];

                for (int i = 0; i < SpotContent.ITEMS.size(); i++){
                    SpotList[i] = SpotContent.ITEMS.get(i).toString();
                }

                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(FindersActivity.this, android.R.layout.simple_list_item_1,SpotList );


                ListView listView = (ListView) findViewById(R.id.spot_list);
                listView.setAdapter(itemsAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

        /*String[] SpotList = new String[SpotContent.ITEMS.size()];

        for (int i = 0; i < SpotContent.ITEMS.size(); i++){
            SpotList[i] = SpotContent.ITEMS.get(i).toString();
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,SpotList );


        ListView listView = (ListView) findViewById(R.id.spot_list);
        listView.setAdapter(itemsAdapter);*/

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
