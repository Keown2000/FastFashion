package keown.cantrell.grade.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import keown.cantrell.grade.R;

public class profile extends AppCompatActivity {
    TextView tvname,tvusername,tvemail,tvphone,tvbio,tvedit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    personalinfo personalinfo;
    ImageView imageView;

    private int STORAGE_PERMISSION_CODE = 1;
    public static  String pname="";
    public static  String pusername="";
    public static  String pphone="";
    public static  String pemail="";
    public static  String pbio="";
    public static  String pimage="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        tvname=findViewById(R.id.viewname);
        tvusername=findViewById(R.id.viewusername);
        tvemail=findViewById(R.id.viewemail);
        tvphone=findViewById(R.id.viewphone);
        tvbio=findViewById(R.id.viewbio);
        tvedit=findViewById(R.id.edit);
        imageView=findViewById(R.id.imageprofile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("data");
///functions

        tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), edit.class));
                //getActivity().finish();
            }
        });
        ////fetching from firebase
        personalinfo = new personalinfo();
        databaseReference.addValueEventListener(new ValueEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    personalinfo detail = dataSnapshot.getValue(personalinfo.class);

                    String name = detail.getName();
                    String username = detail.getUsername();
                    String email = detail.getEmail();
                    String phone = detail.getPhone();
                    String bio = detail.getBio();
                    String imageurl = detail.getImageUrl();

                    if (name!=null) {
                        tvname.setText(name);
                    }
                    if (username!=null) {
                        tvusername.setText(username);
                    }
                    if (email!=null) {
                        tvemail.setText(email);
                    }
                    if (phone!=null) {
                        tvphone.setText(phone);
                    }
                    if (bio!=null) {
                        tvbio.setText(bio);
                    }

                    pname=name;
                    pusername=username;
                    pphone=phone;
                    pemail=email;
                    pbio=bio;
                    pimage=imageurl;
//
                    if (imageurl!=null && !imageurl.isEmpty()) {
                        Picasso.get().load(imageurl).into(imageView);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // [START_EXCLUDE]
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        // home selected as default
        bottomNavigationView.setSelectedItemId(R.id.second);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.third:
                        startActivity(new Intent(getApplicationContext(),ContactMe.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.first:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.fourth:
                        startActivity(new Intent(getApplicationContext(),ImagesActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                }

                return false;
            }
        });
    }

    ////storage permission
    private void checkstorage() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {



        } else {
            requestStoragePermission();
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    //////storage permissions
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(profile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because all stuffs you use in based on your current location")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(profile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(profile.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(profile.this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(profile.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
