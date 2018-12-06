package com.example.rosa.finderseekers;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeekersActivity extends BaseActivity {

    private Spinner state, city;
    private Button btnSubmit;
    DatabaseReference topRef;


    @Override
    int getContentViewId() {
        return R.layout.activity_seekers2;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_notifications;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Clicked","Seekers Open");
        Log.i("USER", MainActivity.EMAIL);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        topRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference spots = topRef.child("Spots");

        spots.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SpotContent.ITEMS.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
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
                String[] SpotList = new String[SpotContent.ITEMS.size()];

                for (int i = 0; i < SpotContent.ITEMS.size(); i++){
                    SpotList[i] = SpotContent.ITEMS.get(i).toString();
                }

                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(SeekersActivity.this, android.R.layout.simple_list_item_1,SpotList );


                ListView listView = (ListView) findViewById(R.id.seekerSpotList);
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



    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        city = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("Grand Rapids");
        list.add("Detroit");
        list.add("Flint");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        state = (Spinner) findViewById(R.id.spinner1);
        state.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        state = (Spinner) findViewById(R.id.spinner1);
        city = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatabaseReference newList = topRef.child("Spots");

                newList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SpotContent.ITEMS.clear();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(ds.getValue(SpotContent.SpotItem.class).getState().equals(String.valueOf(state.getSelectedItem()))&& ds.getValue(SpotContent.SpotItem.class).getCity().equals(city.getSelectedItem()))
                            {
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
                                new ArrayAdapter<String>(SeekersActivity.this, android.R.layout.simple_list_item_1,SpotList );


                        ListView listView = (ListView) findViewById(R.id.seekerSpotList);
                        listView.setAdapter(itemsAdapter);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FindData", "onCancelled", databaseError.toException());
                    }
                });

            }

        });
    }

}
