package keown.cantrell.grade.activities;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import keown.cantrell.grade.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_val.*

class ValActivity : AppCompatActivity() {       //applies features to action bars Alternate constructor that can
                                                // be used to provide a default layout that will be inflated as part of super.onCreate(savedInstanceState).

    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_val)
        auth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener {

            if(editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString().isNotEmpty()){  //trim eliminates leading and trailing spaces.
                createUser(editEmail.text.trim().toString(), editPassword.text.trim().toString())

            }else{
                Toast.makeText(this,"Input Required",Toast.LENGTH_LONG).show()

            }


        }

        tvLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent)
        }
    }


    fun createUser(email:String, password:String){

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener (this){ task ->

                if(task.isSuccessful){
                    Log.e("Task Message", "Successful ...");

                    var intent = Intent(this,MainActivity::class.java);
                    startActivity(intent);

                }else{
                    Log.e("Task Message", "Failed ..."+task.exception);

                }

            }
    }
    }
