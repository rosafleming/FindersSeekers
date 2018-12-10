package com.example.rosa.finderseekers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import static java.lang.Thread.sleep;

public class SeekersActivity extends BaseActivity {

    private Spinner state, city;
    private Button btnSubmit;
    DatabaseReference topRef;
    DatabaseReference users;
    ListView listView;
    private String currentState;
    private String selected_city;
    private ArrayAdapter<String> dataAdapter;
    private List<String> city_list;

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

        currentState = "";
        selected_city = "";
        city = (Spinner) findViewById(R.id.spinner2);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        topRef = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.seekerSpotList);

        users = topRef.child("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if((Integer.parseInt(ds.getValue(ProfileContent.ContentItem.class).getSeekersNum()) - Integer.parseInt(ds.getValue(ProfileContent.ContentItem.class).getFindersNum())) > 5) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SeekersActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please Add More Spots Before Seeking.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(SeekersActivity.this, FindersActivity.class);
                                        startActivity (intent);
                                    }
                                });
                        alertDialog.show();

                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

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

                listView.setAdapter(itemsAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users = topRef.child("Users");
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            if(ds.getValue(ProfileContent.ContentItem.class).getEmail().equals(MainActivity.EMAIL)) {
                                int seekerCounter = Integer.parseInt(ds.getValue(ProfileContent.ContentItem.class).getSeekersNum());
                                seekerCounter=seekerCounter+1;
                                ds.getRef().child("seekersNum").setValue(Integer.toString(seekerCounter));
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FindData", "onCancelled", databaseError.toException());
                    }
                });

                Intent intent = new Intent(SeekersActivity.this, SpotSelected.class);
                startActivity (intent);
            }
        });


    }



    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        city_list = new ArrayList<String>();

        topRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cities = topRef.child("Spots");

        cities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //SpotContent.ITEMS.clear();
                Boolean cityExists = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getValue(SpotContent.SpotItem.class).getState().equals(String.valueOf(state.getSelectedItem()))){
                        for(int i = 0; i < city_list.size(); i++) {
                            if(city_list.get(i).equals(ds.getValue(SpotContent.SpotItem.class).getCity())){
                                cityExists = true;
                            }
                        }
                        if(cityExists == true){
                            cityExists = false;
                        }else{
                            city_list.add(ds.getValue(SpotContent.SpotItem.class).getCity());
                        }
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, city_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);

        //city.setSelection(0);
    }

    public void addListenerOnSpinnerItemSelection() {
        state = (Spinner) findViewById(R.id.spinner1);
        state.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!String.valueOf(state.getSelectedItem()).equals(currentState)){
                    Log.i("STATE_SWITCH", "Ran This");


                    topRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference cities = topRef.child("Spots");

                    cities.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            city_list.clear();
                            Boolean cityExists = false;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                if(ds.getValue(SpotContent.SpotItem.class).getState().equals(String.valueOf(state.getSelectedItem()))){
                                    for(int i = 0; i < city_list.size(); i++) {
                                        if(city_list.get(i).equals(ds.getValue(SpotContent.SpotItem.class).getCity())){
                                            cityExists = true;
                                        }
                                    }
                                    if(cityExists == true){
                                        cityExists = false;
                                    }else{
                                        city_list.add(ds.getValue(SpotContent.SpotItem.class).getCity());
                                    }
                                }
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("FindData", "onCancelled", databaseError.toException());
                        }
                    });

                    dataAdapter.notifyDataSetChanged();
                    city.setSelection(0);
                    Log.i("STATE_SWITCH", "Notified");

                    city.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            selected_city = String.valueOf(city.getSelectedItem());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }
                    });

                    currentState = String.valueOf(state.getSelectedItem());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
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
                Log.i("STATE_CITY", String.valueOf(state.getSelectedItem()) + " and " + selected_city);

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
