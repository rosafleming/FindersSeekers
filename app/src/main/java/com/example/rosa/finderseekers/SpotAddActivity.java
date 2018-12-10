package com.example.rosa.finderseekers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SpotAddActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1888;

    private Spinner state;
    private Button post;
    private Button imageUpload;
    private EditText name;
    private EditText description;
    private EditText directions;
    private EditText city;
    private ImageView locPic;

    DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_add);
        //Log.i("Clicked","Finders Open");
        city = findViewById(R.id.city_input);
        locPic = (ImageView) findViewById(R.id.locPic);
        imageUpload = (Button) findViewById(R.id.imageUpload);

        //addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

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

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference profileRef = storage.getReference();
                StorageReference profileImagesRef = profileRef.child("locations/" + MainActivity.EMAIL +"_locpic.png");

                Bitmap bitmap = ((BitmapDrawable) locPic.getDrawable()).getBitmap();
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

                Intent intent = new Intent(SpotAddActivity.this, FindersActivity.class);
                startActivity (intent);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //local
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            locPic.setImageBitmap(imageBitmap);
            locPic.setDrawingCacheEnabled(true);
            locPic.buildDrawingCache();


        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
