package com.example.p5projectchat.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.p5projectchat.ProjectManagement.FirstPageActivity;
import com.example.p5projectchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Intent intent = new Intent();
    EditText emailEdit;
    EditText passEdit;

    String email;
    String password;

    int toastDuration = Toast.LENGTH_SHORT;

    CharSequence wrongEmailAndPass = "The Email and/or Pasword does not exist";

    private String emailFromCreateAccount;
    private static final String EXSTRA_ID = "emailID";
    private static final String SAVE_EMAIL = "saveEMAIL";

    String TAG = "Firebase";
    String CLASS_TAG = "LoginActivity";

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myDatabaseRef;

    Context context;

    Button loginBtn;
    Button createAccBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText)findViewById(R.id.username);
        passEdit = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.login);
        context = getApplicationContext();

        //Getting email from the Create Account
        emailFromCreateAccount = getIntent().getStringExtra(EXSTRA_ID);
        Log.i("FROM_CREATE", "email: "+ emailFromCreateAccount);

        emailEdit.setText(emailFromCreateAccount);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();


    }

    public void loginButton(View view){
        email = emailEdit.getText().toString();
        password = passEdit.getText().toString();

        signInWithFirebase(email, password);

        intent.setClass(this, FirstPageActivity.class);

    }


    public void createAccountButton(View view){
        startActivity(new Intent(this, CreateAccountActivity.class));
        finish();
    }

    public void signInWithFirebase(final String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:SUCCESS");

                            startActivity(intent);

                            finish();
                        } else{
                            Log.w(TAG, "signInWithEmail:FAILURE", task.getException());
                            Toast.makeText(context, wrongEmailAndPass, toastDuration).show();
                            emailEdit.setError(wrongEmailAndPass);
                        }
                    }
                });
    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.i(CLASS_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        SharedPreferences prefs = getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE);
        String restoredEmail = prefs.getString("email", null);
        Log.i(CLASS_TAG, "email from pref: " + restoredEmail);
        if (restoredEmail != null && emailFromCreateAccount == null){
            emailEdit.setText(restoredEmail);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE).edit();
        editor.putString("email", emailEdit.getText().toString());
        editor.apply();
        Log.i(CLASS_TAG, "The editor email in onPause: " + emailEdit.getText().toString());
        Log.i(CLASS_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(CLASS_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(CLASS_TAG, "onDestroy");
    }

}
