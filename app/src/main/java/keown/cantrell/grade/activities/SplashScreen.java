package keown.cantrell.grade.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;



import keown.cantrell.grade.R;

public class SplashScreen extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
lottieAnimationView = findViewById(R.id.lottie);

lottieAnimationView.animate().translationY(2000).setDuration(500).setStartDelay(700);
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(keown.cantrell.grade.activities.SplashScreen.this,
                   LoginActivity.class )
           .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
            finish();
        }
    },1100); 
    }

}