package keown.cantrell.grade.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import keown.cantrell.grade.R;

public class edit extends AppCompatActivity {
    EditText etname, etusername, etemail, etphone, etbio;
    Button btsave;
    ImageView btimage;
    /////firebase componetrts
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    personalinfo personalinfo;

    //
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    // request code

    private Uri photouri;
    String downloadurl;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //typecasting
        etname = findViewById(R.id.txtname);
        etusername = findViewById(R.id.txtusername);
        etemail = findViewById(R.id.txtemail);
        etphone = findViewById(R.id.txtphone);
        etbio = findViewById(R.id.txtbio);
        btsave = findViewById(R.id.btnsave);
        btimage = findViewById(R.id.imagebtn);

        //putting existing values in textbix
        etname.setText(profile.pname);
        etusername.setText(profile.pusername);
        etbio.setText(profile.pbio);
        etemail.setText(profile.pemail);
        etphone.setText(profile.pphone);
        if (profile.pimage!=null && !profile.pimage.isEmpty()) {
            Picasso.get().load(profile.pimage).into(btimage);}


        ///////functions start
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        personalinfo = new personalinfo();

//on save btn click
        btsave.setOnClickListener(view1 -> {

            String name = etname.getText().toString();
            String username = etusername.getText().toString();
            String bio = etbio.getText().toString();
            String email = etemail.getText().toString();
            String phone = etphone.getText().toString();
            //check if field are validated or non epmty(later white code)
            if (name.length() == 0) {
                etname.setError("Required");
                etname.requestFocus();
                return;
            }
            if (username.length() == 0) {
                etusername.setError("Required");
                etusername.requestFocus();
                return;
            }
            if (email.length() == 0) {
                etemail.setError("Required");
                etemail.requestFocus();
                return;
            }
            if (phone.length() == 0) {
                etphone.setError("Required");
                etphone.requestFocus();
                return;
            }
            if (bio.length() == 0) {
                etbio.setError("Required");
                etbio.requestFocus();
                return;
            }

            if (photouri != null) {
                uploadtofirebase(name, username, email, phone, bio);
            } else insertdata(name, username, email, phone, bio, profile.pimage);


        });

//image upload function
        btimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//select image n,not upload image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            photouri = data.getData();
            //  imageView.setImageURI(photouri);
//            Snackbar.make(findViewById(R.id.rootlayout), "Photo Upload Success भन्ने मेसेज आएपछि मात्र 'सेभ गर्नुहोस' बटन थिच्नु होला", Snackbar.LENGTH_LONG).show();
            //  uploadtofirebase();


        }
    }

    public void insertdata(String name, String username, String email, String phone, String bio, String imageurl) {
        personalinfo.setName(name);
        personalinfo.setEmail(email);
        personalinfo.setPhone(phone);
        personalinfo.setUsername(username);
        personalinfo.setBio(bio);
        personalinfo.setImageUrl(imageurl);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child("data").setValue(personalinfo);
                Toast.makeText(getApplicationContext(), "Details Inserted", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(keown.cantrell.grade.activities.edit.this, profile.class));
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Toast.makeText(getApplicationContext(), "Insert Failed!!", Toast.LENGTH_SHORT).show();

            }
        });

    }


    //function that uploads image to firebase cloud storage
    // UploadImage method
    private void uploadtofirebase(String name, String username, String email, String phone, String bio) {
        progressDialog = ProgressDialog.show(keown.cantrell.grade.activities.edit.this, "", "Uploading Photo...", true);

        StorageReference riversRef = storageReference.child("images/" + UUID.randomUUID()); //+ UUID.randomUUID()

        riversRef.putFile(photouri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get a URL to the uploaded content
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        downloadurl = uri.toString();
                        progressDialog.dismiss();
                        insertdata(name, username, email, phone, bio, downloadurl);


                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    // ...
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Photo upload Failed", Toast.LENGTH_SHORT).show();
                });

    }
}