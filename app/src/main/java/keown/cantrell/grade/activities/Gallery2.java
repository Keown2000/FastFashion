package keown.cantrell.grade.activities;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import keown.cantrell.grade.R;


public class Gallery2 extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    private static final int PICK_IMAGE_REQUEST = 1;                           //constant //use this number to identify our image requests
    private Button mButtonChooseImage;                                         //variables
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;                                                      //Uri that will point to our image soo it will show in image view and upload to firebase storage


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);                                        //view variables
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");        //data will be saved in a file named uploads in the firebase storage
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");         //data will be saved in a file named uploads in the firebase database



        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();                              //opens file chooser through function
            }
        });


        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Gallery2.this, "Upload in progress", Toast.LENGTH_SHORT).show();  //if upload in progress, toast message will be presented
                } else {
                    uploadFile();                                                                                    //upload button function
                }
            }
        });


        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");                                                     //makes sure that only images are shown
        intent.setAction(Intent.ACTION_GET_CONTENT);                                //file chooser function
        startActivityForResult(intent, PICK_IMAGE_REQUEST);                         //in order to pass data it must be identified in some way
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK        //checking for image requests, if the user has picked an image and actually get something back
                && data != null && data.getData() != null) {
            mImageUri = data.getData();                                         //after image is picked we get back the Uri, this variable now contains the uri of the variable which is then used to set it to image view and firebase
            Picasso.get().load(mImageUri).into(mImageView);                         //picasso is an sdk used to pass image into image view.
        }
    }

    private String getFileExtension(Uri uri) {                                              //returns extension of file picked
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()       //makes sure that files will not have the same name using milliseconds
                    + "." + getFileExtension(mImageUri));                                       //adds file extension to the end of file name
            mUploadTask = fileReference.putFile(mImageUri)                                     //puts file on reference
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();                        //use handler to delay reset of progress bar by few seconds
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0); }              //resets progress bar to 0
                            }, 500);
                            Toast.makeText(Gallery2.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();                                 //creating new upload item
                            while (!urlTask.isSuccessful());                                                                  //if the task is success
                            Uri downloadUrl = urlTask.getResult();                                                              //gets result of uri

                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString());       //takes the content of edit text and download url

                            String uploadId = mDatabaseRef.push().getKey();              //makes entry in the database
                            mDatabaseRef.child(uploadId).setValue(upload);              //takes unique ID and uploads it to the upload file in firebase

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Gallery2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })


                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());  //gets transfer bytes in relation to total bytes//
                            mProgressBar.setProgress((int) progress);                                                              //pass the progress
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }
    private void openImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }
}