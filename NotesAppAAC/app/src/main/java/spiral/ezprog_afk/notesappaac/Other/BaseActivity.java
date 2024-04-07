package spiral.ezprog_afk.notesappaac.Other;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import spiral.ezprog_afk.notesappaac.BottomActivities.MainActivity;
import spiral.ezprog_afk.notesappaac.BottomActivities.ProfileActivity;
import spiral.ezprog_afk.notesappaac.BottomActivities.CompletedActivity;
import spiral.ezprog_afk.notesappaac.BottomActivities.ToDoShopActivity;
import spiral.ezprog_afk.notesappaac.R;

public abstract class BaseActivity extends AppCompatActivity {

    //Vars:

    private static BottomNavigationViewEx bottomNavigationView;

    public static BottomNavigationViewEx getBottomNav() {
        return bottomNavigationView;
    }

    public static void hideBottomNav() {
        bottomNavigationView.setVisibility(View.INVISIBLE);
    }

    public static void unHideBottomNav() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }


    public void setUpBottomNav() {
        bottomNavigationView = findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setTextVisibility(true);
        bottomNavigationView.setTextSize(15);
        bottomNavigationView.enableItemShiftingMode(true);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableAnimation(false);

        for (int i = 0; i < bottomNavigationView.getItemCount(); i++) {
            bottomNavigationView.setIconTintList(i, null);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_item_add_task:
                        Intent toAddIntent = new Intent(getApplicationContext(), MainActivity.class);
                        toAddIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        startActivity(toAddIntent);
                        return true;
                    case R.id.nav_item_profile:
                        Intent toProfileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                        toProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        startActivity(toProfileIntent);
                        return true;
                    case R.id.nav_item_calendar_task:
                        Intent toAddTimeTaskIntent = new Intent(getApplicationContext(), CompletedActivity.class);
                        toAddTimeTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        startActivity(toAddTimeTaskIntent);
                        return true;
                    case R.id.nav_item_todo:
                        Intent toToDoIntent = new Intent(getApplicationContext(), ToDoShopActivity.class);
                        toToDoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        startActivity(toToDoIntent);
                        return true;
                }
                return false;
            }
        });

    }

}
