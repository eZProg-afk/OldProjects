package spiral.ezprog_afk.notesappaac.BottomActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import spiral.ezprog_afk.notesappaac.Other.BaseActivity;
import spiral.ezprog_afk.notesappaac.AddEditActivities.EditProfileActivity;
import spiral.ezprog_afk.notesappaac.R;
import spiral.ezprog_afk.notesappaac.Settings.SettingsActivity;

public class ProfileActivity extends BaseActivity {

    //Vars:

    private Button editProfileBtn;
    private TextView countOfTasks, countOfPurchases, countOfTODOs, warningTV, usernameTextView;
    private SharedPreferences sharedPreferencesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpBottomNav();
        editProfileBtn = findViewById(R.id.edit_profile_button);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(editIntent);
            }
        });

        countOfTasks = findViewById(R.id.completed_tasks_tv);
        countOfPurchases = findViewById(R.id.completed_purchases_tv);
        countOfTODOs = findViewById(R.id.completed_to_do_tv);
        warningTV = findViewById(R.id.warning_text_view);
        usernameTextView = findViewById(R.id.username_text_view);

        sharedPreferencesApp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String username = sharedPreferencesApp.getString("username", "username");
        usernameTextView.setText(username);
        if (sharedPreferencesApp.getBoolean("statistic", false)) {
            String countNotes = String.valueOf(sharedPreferencesApp.getInt("countOfCompletedNotes", 0));
            countOfTasks.setText("Число завершенных задач: " + countNotes);
            String countTODO = String.valueOf(sharedPreferencesApp.getInt("countOfCompletedTODO", 0));
            countOfTODOs.setText("Число выполненных TODO: " + countTODO);
            String countPurchases = String.valueOf(sharedPreferencesApp.getInt("countOfCompletedPurchases", 0));
            countOfPurchases.setText("Число сделанных покупок: " + countPurchases);
        } else {
            countOfTasks.setVisibility(View.GONE);
            countOfTODOs.setVisibility(View.GONE);
            countOfPurchases.setVisibility(View.GONE);
            warningTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}