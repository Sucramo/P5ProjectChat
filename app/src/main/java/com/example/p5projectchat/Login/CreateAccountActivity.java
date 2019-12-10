package com.example.p5projectchat.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p5projectchat.Database.User;
import com.example.p5projectchat.Login.LoginActivity;
import com.example.p5projectchat.R;
import com.example.p5projectchat.Security.AES;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;

import static com.example.p5projectchat.UserSettings.EditUserActivity.rotateBitmap;

public class CreateAccountActivity extends AppCompatActivity {

    Intent intent = new Intent();
    Context context;

    private String EXTRA_ID = "emailID";

    CharSequence missingInfoError = "Please fill out all the fields above";
    CharSequence passwordNotMatching = "Passwords are not matching";
    CharSequence emailNotValid = "please provide a valid email...";
    CharSequence emailAlreadyExist = "the email provided already exists";
    CharSequence nothing = "";
    CharSequence permission = "Please allow the app to use the following";

    int toastDuration = Toast.LENGTH_SHORT;
    Toast missingInfoToast;
    Toast wrongPasswordToast;
    Toast emailNotValidToast;
    Toast emailAlreadyExistToast;
    Toast permissionToast;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPassword;
    EditText editConfirmPassword;

    String firstName;
    String lastName;
    String email;
    String password;
    String confirmPassword;
    String picturePath = null;
    TextView uploadPhotoView;
    private EditText editLoginEmail;
    private Bitmap loadedBitmap;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String TAG = "Firebase";
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseRef;
    String userIdString;
    int userID = 0;

    private static int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;
    ImageButton imageButton;
    Button createAccountButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setHideKeyboardOnTouch(this, findViewById(R.id.activity_create_account));

        editFirstName =  (EditText)findViewById(R.id.firstNameEdit);
        editLastName = (EditText)findViewById(R.id.lastNameEdit);
        editEmail = (EditText)findViewById(R.id.emailEdit);
        editPassword = (EditText)findViewById(R.id.passEdit);
        editConfirmPassword = (EditText)findViewById(R.id.confirmPassEdit);
        context = getApplicationContext();
        createAccountButton = (Button)findViewById(R.id.createAccountButton);

        //Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();

        // make app update data in real time.
    }

    public void createAccountButtonClicked (View view){
        firstName = editFirstName.getText().toString();
        lastName = editLastName.getText().toString();
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();
        missingInfoToast = Toast.makeText(context, missingInfoError, toastDuration);
        wrongPasswordToast = Toast.makeText(context, passwordNotMatching, toastDuration);
        emailAlreadyExistToast = Toast.makeText(context, emailAlreadyExist, toastDuration);
        emailNotValidToast = Toast.makeText(context, emailNotValid, toastDuration);

        if(firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("")){
            missingInfoToast.show();
        } else if(!password.equals(confirmPassword)){
            editPassword.setText("");
            editConfirmPassword.setText("");
            editConfirmPassword.setError("Password doesn't match.");
            wrongPasswordToast.show();
        } else if (password.length() < 8){
            editPassword.setError("Password must contain at least 8 characters");

        } else if (!isValidEmail(email)){
            editEmail.setError("This is not a valid email."); // Does not check if the email exist. Only the format example@123.aaa
            emailNotValidToast.show();
        } else{
            createUserFirebase(email, password);
        }
    }

    public void addProfilePictureButtonClicked(View view){
        if (checkGalleryPermissions()){
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            loadedBitmap = BitmapFactory.decodeFile(picturePath);

            // Checking rotation of image and turning it the right way
            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null) {
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            //

            imageView.setImageBitmap(loadedBitmap);
            uploadPhotoView.setText(nothing);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public static void setHideKeyboardOnTouch(final Context context, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return false;
                    }

                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setHideKeyboardOnTouch(context, innerView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkGalleryPermissions(){
        // Here, this is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                return false;

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE);
                return true;
            }
        } else {
            // Permission has already been granted
            return true;
        }
    }

    private void createUserFirebase(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the used information
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                            onAuthSuccess(task.getResult().getUser());
                            auth.signOut();

                        } else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Create account failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String pass = AES.encryptNumberTwo(password, password);
        // Write new user
        writeNewUser(firstName, lastName, email, pass, user.getUid(), false);
        // Go to FirstPageActivity
        intent = new Intent (this, LoginActivity.class);
        intent.putExtra(EXTRA_ID, email);
        startActivity(intent);
        finish();
    }

    private void writeNewUser(String firstName, String lastName, String email, String password, String userID, boolean isLoggedIn) {

        User user = new User(firstName, lastName, email, password, userID, isLoggedIn);
        myDatabaseRef.child("users").child(userID).setValue(user);
    }

    public static boolean isValidEmail(CharSequence target){
        if (target == null){
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
