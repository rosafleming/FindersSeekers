package com.example.rosa.finderseekers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class ProfileActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    DatabaseReference topRef;
    ProfileContent.ContentItem user;

    @Override
    int getContentViewId() {
        return R.layout.activity_profile;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView profilename = (TextView) findViewById(R.id.nameId);
        TextView SeekersNum = (TextView) findViewById(R.id.SeekersNum);
        TextView FindersNum = (TextView) findViewById(R.id.FindersNum);
        TextView email = (TextView) findViewById(R.id.emailAddr);
        Button logoutButton = (Button) findViewById(R.id.logout);
        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener( v-> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity (intent);
        });

        topRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userList = topRef.child("Users");


        Log.i("PROFILE", "Made It");
        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("PROFILE", "HERE");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getValue(ProfileContent.ContentItem.class).getEmail().equals(MainActivity.EMAIL))
                    {
                        Log.i("PROFILE", "HERE");
                        profilename.setText(ds.getValue(ProfileContent.ContentItem.class).getName());
                        email.setText(ds.getValue(ProfileContent.ContentItem.class).getEmail());
                        SeekersNum.setText(ds.getValue(ProfileContent.ContentItem.class).getSeekersNum());
                        FindersNum.setText(ds.getValue(ProfileContent.ContentItem.class).getFindersNum());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FindData", "onCancelled", databaseError.toException());
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference profileRef = storage.getReference();
        StorageReference profileImagesRef = profileRef.child("images/" + email.getText().toString()+"_profilepic.png");

        final long ONE_MEGABYTE = 1024 * 1024;
        profileImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //byte[] bitmapdata; // let this be your byte array
                Bitmap downloadedBit = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                profilePic.setImageBitmap(downloadedBit);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
