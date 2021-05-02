package keown.cantrell.grade.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import keown.cantrell.grade.R;



public class ContactMe extends AppCompatActivity {
    TextInputLayout regName, regMessage, regEmail, regPhoneNo;    //Layout which wraps a TextInputEditText, EditText, or descendant to show a floating label when the hint is hidden while the user inputs text.
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode;                      // calling FirebaseDatabase as rootNode //rootNode has no parent
    DatabaseReference reference;                  //calling DatabaseReference as  reference



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);

        regName = findViewById(R.id.reg_name);
        regMessage = findViewById(R.id.reg_message);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phoneNo);
        regBtn = findViewById(R.id.Regbtn);
        regBtn.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();               //Gets a FirebaseDatabase instance for the specified URL
                reference = rootNode.getReference("Contacts");      //Gets a DatabaseReference for the database root node.
                String name = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String message = regMessage.getEditText().getText().toString();
                String PhoneNo = regPhoneNo.getEditText().getText().toString();
                UserHelperClass1 helperClass = new UserHelperClass1(name, message, email, PhoneNo);  //
                reference.child(name).setValue(helperClass);
                Toast.makeText(getApplicationContext(), "Message Sent!! You can expect a reply very soon", Toast.LENGTH_SHORT).show();
            }
        });




        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        // home selected as default
        bottomNavigationView.setSelectedItemId(R.id.third);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.second:
                        startActivity(new Intent(getApplicationContext(),profile.class));
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
}