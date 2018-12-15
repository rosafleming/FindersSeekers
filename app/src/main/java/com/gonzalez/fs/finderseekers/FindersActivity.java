package com.gonzalez.fs.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindersActivity extends BaseActivity {
    private Button post;

    DatabaseReference topRef;
    DatabaseReference spots;
    DatabaseReference users;
    ListView listView;

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
        listView = (ListView) findViewById(R.id.spot_list);

        topRef = FirebaseDatabase.getInstance().getReference();
        spots = topRef.child("Spots");



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
                String title =  (String)parent.getItemAtPosition(position);
                String delims = "[ ]+";
                String result[] = title.split(delims);


                String spotTitle="";

                for(int i = 1; i < result.length; i++){
                    if(result[i].equals("\nState:")){
                        break;
                    }
                    Log.i("SEEKSPOT", result[i]);
                    spotTitle = spotTitle + " " + result[i];
                }

                Log.i("SEEKSPOT",spotTitle );
                Intent intent = new Intent(FindersActivity.this, SpotSelected.class);
                intent.putExtra("selectedTitle", spotTitle);
                startActivity (intent);
            }
        });

    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        post = (Button) findViewById(R.id.postbutton);

        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                users = topRef.child("Users");
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(ds.getValue(ProfileContent.ContentItem.class).getEmail().equals(MainActivity.EMAIL)) {
                                int finderCounter = Integer.parseInt(ds.getValue(ProfileContent.ContentItem.class).getFindersNum());
                                finderCounter=finderCounter+1;
                                ds.getRef().child("findersNum").setValue(Integer.toString(finderCounter));
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FindData", "onCancelled", databaseError.toException());
                    }
                });

                Intent intent = new Intent(FindersActivity.this, SpotAddActivity.class);
                startActivity (intent);
            }

        });
    }

}
