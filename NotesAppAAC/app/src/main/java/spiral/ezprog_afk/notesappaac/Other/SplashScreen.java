package spiral.ezprog_afk.notesappaac.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import spiral.ezprog_afk.notesappaac.BottomActivities.MainActivity;
import spiral.ezprog_afk.notesappaac.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentBack = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intentBack);
                finish();
            }
        }, 2000);
    }
}
