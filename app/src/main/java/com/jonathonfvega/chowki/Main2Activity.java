package com.jonathonfvega.chowki;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import static android.media.ExifInterface.TAG_ARTIST;
import static android.media.ExifInterface.TAG_IMAGE_UNIQUE_ID;

public class Main2Activity extends AppCompatActivity {

    private static final int SELECTED_PIC=1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void btnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PIC:
                if (resultCode==RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filepath=cursor.getString(columnIndex);
                    Log.d("Hello", "This is here!!!!" + filepath);

                    try {
                        ExifInterface exif = new ExifInterface(filepath);
                        //System.out.print(exif.getAttribute(ExifInterface.TAG_DATETIME));
                        Log.d("Lets see", exif.getAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID));

                        exif.setAttribute(TAG_IMAGE_UNIQUE_ID, "uniqueID1");
                        exif.saveAttributes();




                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("ImageData").child("89234jf8a9sf4").child("UserUID");

                        myRef.setValue(getCurrentUserID());




                    } catch (IOException e) {
                        Log.d("Something messed up", "Exif error");
                    }

                    try {
                        Log.d("", new ExifInterface(filepath).getAttribute(TAG_IMAGE_UNIQUE_ID));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                    Drawable drawable = new BitmapDrawable(bitmap);
                }
                break;
            default:
                break;
        }


    }

    private String getCurrentUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

            return user.getUid();

        } else {
            // No user is signed in
            System.out.print("No User");
        }

        return null;
    }

}
