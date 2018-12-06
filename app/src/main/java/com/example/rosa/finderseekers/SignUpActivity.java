package com.example.rosa.finderseekers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;
    DatabaseReference topRef;

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

        imageUpload.setOnClickListener( v-> {
            Intent intent = new Intent(SignUpActivity.this, CameraActivity.class);
            startActivity (intent);
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

}
