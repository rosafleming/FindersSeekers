package com.gonzalez.fs.finderseekers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        spotPic = findViewById(R.id.spotPic);

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

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference profileRef = storage.getReference();
                        StorageReference profileImagesRef = profileRef.child("locations/" + spotname.getText().toString() +"_locpic.png");


                        final long ONE_MEGABYTE = 1024 * 1024;
                        profileImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                //byte[] bitmapdata; // let this be your byte array
                                Bitmap downloadedBit = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                                spotPic.setImageBitmap(downloadedBit);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

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
