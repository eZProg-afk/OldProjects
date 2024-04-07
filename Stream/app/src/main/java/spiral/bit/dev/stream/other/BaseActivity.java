package spiral.bit.dev.stream.other;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.activities.ContactsActivity;
import spiral.bit.dev.stream.activities.DashboardActivity;
import spiral.bit.dev.stream.activities.SettingsActivity;

public class BaseActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    public void setUpBottomNavView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton fabHome = findViewById(R.id.fab_home);
        fabHome.setOnClickListener(v -> {
            Intent toConferenceIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            toConferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(toConferenceIntent);
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_item:
                    Intent toConferenceIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                    toConferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    startActivity(toConferenceIntent);
                    return true;
                case R.id.settings_item:
                    Intent toSettingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                    toSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    startActivity(toSettingsIntent);
                    return true;
                case R.id.contacts_item:
                    Intent toContactsIntent = new Intent(getApplicationContext(), ContactsActivity.class);
                    toContactsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    startActivity(toContactsIntent);
                    return true;
            }
            return false;
        });
    }
}
