package com.example.rosa.finderseekers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpotSelected extends AppCompatActivity {

    ImageView spotPic;
    TextView spotname;
    TextView username;
    TextView state;
    TextView city;
    TextView description;
    TextView direction;
    String selectedTitle;

    DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_selected);

        Intent intent = getIntent();
        selectedTitle = intent.getStringExtra("selectedTitle");

        spotname = findViewById(R.id.spotTitle);
        username = findViewById(R.id.spotUsername);
        state = findViewById(R.id.spotState);
        city = findViewById(R.id.spotCity);
        description = findViewById(R.id.spotDesc);
        direction = findViewById(R.id.spotDirec);

        topRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference spots = topRef.child("Spots");

        Log.i("SEEKCLICKED", selectedTitle);
        spots.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if((" "+ds.getValue(SpotContent.SpotItem.class).getName()).equals(selectedTitle)) {
                        spotname.setText(ds.getValue(SpotContent.SpotItem.class).getName());
                        username.setText(ds.getValue(SpotContent.SpotItem.class).getUsername());
                        state.setText(ds.getValue(SpotContent.SpotItem.class).getState());
                        city.setText(ds.getValue(SpotContent.SpotItem.class).getCity());
                        description.setText(ds.getValue(SpotContent.SpotItem.class).getDescription());
                        direction.setText(ds.getValue(SpotContent.SpotItem.class).getDirections());

                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

    }
}
