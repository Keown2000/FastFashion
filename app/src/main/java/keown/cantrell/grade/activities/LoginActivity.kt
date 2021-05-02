package keown.cantrell.grade.activities;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import keown.cantrell.grade.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {                    //applies features to action bars Alternate constructor that can
                                                            // be used to provide a default layout that will be inflated as part of super.onCreate(savedInstanceState).


    private lateinit var auth: FirebaseAuth;                // a late initialization of instance of Firebase Authentication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance();                      //obtaining an instance of class by calling getInstance

        tvRegister.setOnClickListener {
            val intent = Intent(this, ValActivity::class.java);     //on click listener for starting registration activity
            startActivity(intent)
        }


        btnLogin.setOnClickListener {
            if(editTextEmail.text.trim().toString().isNotEmpty() || editTextPassword.text.trim().toString().isNotEmpty()){
                signInUser(editTextEmail.text.trim().toString(), editTextPassword.text.trim().toString());                      //if details are not empty the user will be signed in
            }else{
                Toast.makeText(this,"Input required",Toast.LENGTH_LONG).show();                                     //if empty there will be a toast message
            }

        }
    }

    fun signInUser(email:String, password: String){

        auth.signInWithEmailAndPassword(email,password)                                                         //Auth SDK will authenticate users //(this) refers to the current class you are in. The view constructor requires you to provide a context and the only nearest context you have is this which is the current class
            .addOnCompleteListener (this) { task ->                                                                //
                if(task.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent)
                }else{
                    Toast.makeText(this," Error !! "+task.exception, Toast.LENGTH_LONG).show()
                }

            }

    }
}