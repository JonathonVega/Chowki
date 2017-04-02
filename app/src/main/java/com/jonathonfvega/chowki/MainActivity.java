package com.jonathonfvega.chowki;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "Cool";

    private FirebaseAuth mAuth;

    private Boolean signedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        checkForCurrentUser();

        Button buttonSignUp = (Button) findViewById(R.id.signIn);
        buttonSignUp.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void registerUser() {
        String userPhoneNumber = getPhoneNumber();

        String email = userPhoneNumber + "@domain.com";
        String password = "password";

        System.out.print(email);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                            Intent nextActivity = new Intent(MainActivity.this, Main2Activity.class);
                            MainActivity.this.startActivity(nextActivity);

                        } else {
                            Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signInUser() {

        String userPhoneNumber = getPhoneNumber();

        String email = userPhoneNumber + "@domain.com";
        String password = "password";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        signedIn = !signedIn;

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());


                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        signInUser();
        if (signedIn == false) {
            registerUser();
        }

    }

    public void checkForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("There should be a user","hopefully");
            if (user.getEmail().equals((getPhoneNumber() + "@domain.com")) ) {

                // Go to next Activity since user is signed in already

                Intent nextActivity = new Intent(MainActivity.this, Main2Activity.class);
                MainActivity.this.startActivity(nextActivity);

            } else {
                Log.d("IT signed us out", "dafuq");
                mAuth.signOut();
            }
        } else {
            // No user is signed in
            Log.d("IDK","No User");
        }
    }

    public String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneManager = tMgr.getLine1Number();
        Log.d("Maybe it worked", mPhoneManager);
        return mPhoneManager;
    }

}
