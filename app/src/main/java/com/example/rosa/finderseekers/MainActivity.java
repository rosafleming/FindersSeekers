package com.example.rosa.finderseekers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import java.util.regex.Pattern;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import java.util.Arrays;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//https://developers.facebook.com/apps/347284212501597/fb-login/quickstart/
public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    private static final String EMAIL = "email";
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;
    DatabaseReference topRef;

    //Use this with the button listener
    //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = (EditText) findViewById(R.id.user_name);
        EditText password = (EditText) findViewById(R.id.password);
        Button logButton = (Button) findViewById(R.id.loginButton);
        //Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mAuth = FirebaseAuth.getInstance();

        logButton.setOnClickListener(v -> {
            String emailStr = username.getText().toString();
            String passStr = password.getText().toString();
           /* if (emailStr.length() == 0) {
                Snackbar.make(username, R.string.email_required,
                        Snackbar.LENGTH_LONG).show();
                return;
            }
            if (!EMAIL_REGEX.matcher(emailStr).find()) {
                Snackbar.make(username, R.string.incorrect_email,
                        Snackbar.LENGTH_LONG).show();
                return;
            }
            String passStr = password.getText().toString().toLowerCase();
            if (!passStr.contains("password")) {
                return;
            }*/
            /*Snackbar.make(username, "Login verified",
                    Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, SeekersActivity.class);
            intent.putExtra("email",emailStr);
            startActivity (intent);
            finish();*/

            mAuth.signInWithEmailAndPassword(emailStr,passStr)
                    .addOnCompleteListener(this, task -> {
                       if(task.isSuccessful()){
                           Intent toMain = new Intent(this, SeekersActivity.class);
                           toMain.putExtra("email", emailStr);
                           startActivity(toMain);
                           finish();
                       }
                       else{
                           Snackbar.make(username, R.string.incorrect_email,Snackbar.LENGTH_LONG)
                                   .show();
                       }
                    });
            topRef = FirebaseDatabase.getInstance().getReference("Test");
            topRef.push().setValue(emailStr);
        });


        Button register = (Button) findViewById(R.id.signup);
        register.setOnClickListener( v-> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity (intent);
        });




        /*
            Facebook login from here on
        */


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent intent = new Intent(MainActivity.this, SeekersActivity.class);
                        //intent.putExtra("email",EMAIL);
                        startActivity (intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity (intent);
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity (intent);
                        finish();
                    }
                });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();



        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("email",EMAIL);
                startActivity (intent);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    //logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD")); probably wont need

}
