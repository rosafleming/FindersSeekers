package com.example.rosa.finderseekers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1888;
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;
    DatabaseReference topRef;
    ImageView profilePic;

    //FirebaseStorage storage;
    //StorageReference profileRef;
    //StorageReference profileRef = storageRef.child("profilePic");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText username = (EditText) findViewById(R.id.name);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText verifyPasswd = (EditText) findViewById(R.id.password2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button imageUpload = (Button) findViewById(R.id.imageUpload);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });



        mAuth = FirebaseAuth.getInstance();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                String emailStr = email.getText().toString();
                /*if (emailStr.length() == 0) {
                    Snackbar.make(username, R.string.email_required,
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!EMAIL_REGEX.matcher(emailStr).find()) {
                    Snackbar.make(username, R.string.incorrect_email,
                            Snackbar.LENGTH_LONG).show();
                    return;
                }*/
                String passStr = password.getText().toString();
                String verifyPassStr = verifyPasswd.getText().toString();
                /*if (!verifyPassStr.equals(passStr)) {
                    return;
                }
*/

                mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.i("SignUp", "Worked");

                        ProfileContent.ContentItem user = new ProfileContent.ContentItem(
                             username.getText().toString(), emailStr, "0", "0"
                        );
                        ProfileContent.addItem(user);

                        topRef = FirebaseDatabase.getInstance().getReference("Users");
                        topRef.push().setValue(user);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference profileRef = storage.getReference();
                        StorageReference profileImagesRef = profileRef.child("images/" + email.getText().toString()+"_profilepic.png");

                        Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] byteData = baos.toByteArray();

                        UploadTask uploadTask = profileImagesRef.putBytes(byteData);
                        uploadTask.addOnFailureListener(new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception exception){
                                Log.d("myStorage","failure :(");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("myStorage","Success");

                            }
                        });

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("email", emailStr);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        String msg = task.getException().getMessage();
                        Log.i("SignUp", "Broke");
                        Snackbar.make(username, msg, Snackbar.LENGTH_SHORT).show();
                    }

                });


            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference profileRef = storage.getReference();
            StorageReference profileImagesRef = profileRef.child("images/profilepic.png");

            //local
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(imageBitmap);
            profilePic.setDrawingCacheEnabled(true);
            profilePic.buildDrawingCache();

            /*upload
            Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteData = baos.toByteArray();

            UploadTask uploadTask = profileImagesRef.putBytes(byteData);
            uploadTask.addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception exception){
                    Log.d("myStorage","failure :(");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("myStorage","Success");

                }
            });

            //download
            final long ONE_MEGABYTE = 1024 * 1024;
            profileImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    //byte[] bitmapdata; // let this be your byte array
                    Bitmap downloadedBit = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                    profilePic.setImageBitmap(imageBitmap);
                    profilePic.setDrawingCacheEnabled(true);
                    profilePic.buildDrawingCache();
                    // Data for "images/island.jpg" is returns, use this as needed
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            */
        }
    }

}
