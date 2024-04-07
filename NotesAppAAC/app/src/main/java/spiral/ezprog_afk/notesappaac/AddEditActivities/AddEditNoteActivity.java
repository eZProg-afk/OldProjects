package spiral.ezprog_afk.notesappaac.AddEditActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import spiral.ezprog_afk.notesappaac.BottomActivities.MainActivity;
import spiral.ezprog_afk.notesappaac.R;

public class AddEditNoteActivity extends AppCompatActivity {

    //Vars:

    public static final String EXTRA_ID =
            "spiral.ezprog_afk.notesappaac.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "spiral.ezprog_afk.notesappaac.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "spiral.ezprog_afk.notesappaac.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "spiral.ezprog_afk.notesappaac.EXTRA_PRIORITY";
    public static final String EXTRA_DATE =
            "spiral.ezprog_afk.notesappaac.EXTRA_DATE";
    public static final String EXTRA_TIME =
            "spiral.ezprog_afk.notesappaac.EXTRA_TIME";


    private EditText titleEditText, descriptionEditText;
    private TextView hintTextView, textViewViewNote, textViewDescViewNote, textViewDateShow;
    private PublisherAdView mPublisherAdView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Spinner spinnerPriority;
    private CalendarView calendarView;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);
        spinnerPriority = findViewById(R.id.spinnerPriority);

        calendarView = findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                editor.putString("dateNew", date);
                editor.apply();
            }
        });

        hintTextView = findViewById(R.id.hint_text_view);
        textViewViewNote = findViewById(R.id.text_view_view_note);
        textViewDescViewNote = findViewById(R.id.text_view_desc_view_note);
        textViewDateShow = findViewById(R.id.text_view_date_show);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.importance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPriority.setAdapter(adapter);

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.importance);
                if (choose[selectedItemPosition].equals("High") ||
                        choose[selectedItemPosition].equals("Важно")) {
                    editor.putString("priority", getString(R.string.important));
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.importance_toast) + getString(R.string.important), Toast.LENGTH_SHORT);
                    toast.show();
                } else if (choose[selectedItemPosition].equals("Medium") ||
                        choose[selectedItemPosition].equals("Средне")) {
                    editor.putString("priority", getString(R.string.medium));
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.importance_toast) + getString(R.string.medium), Toast.LENGTH_SHORT);
                    toast.show();
                } else if (choose[selectedItemPosition].equals("Low") ||
                        choose[selectedItemPosition].equals("Не так важно")) {
                    editor.putString("priority", getString(R.string.not_so_important));
                    editor.apply();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.importance_toast) + getString(R.string.not_so_important), Toast.LENGTH_SHORT);
                    toast.show();
                } else if (choose[selectedItemPosition].equals("Select a priority") ||
                        choose[selectedItemPosition].equals("Выберите важность")) {
                    Toast.makeText(AddEditNoteActivity.this, R.string.select_a_priority, Toast.LENGTH_SHORT).show();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                editor.putString("priority", getString(R.string.not_so_important));
                editor.apply();
            }
        });
        mPublisherAdView = findViewById(R.id.publisherAdViewNote);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        Intent intent = getIntent();
        boolean isView = sharedPreferences.getBoolean("isView", false);
        if (isView) {
            isView = false;
            editor.remove("isView");
            editor.apply();
            setTitle(getString(R.string.view_note));
            hintTextView.setVisibility(View.GONE);
            spinnerPriority.setVisibility(View.GONE);
            descriptionEditText.setVisibility(View.GONE);
            titleEditText.setVisibility(View.GONE);
            textViewViewNote.setVisibility(View.VISIBLE);
            textViewDescViewNote.setVisibility(View.VISIBLE);
            textViewDateShow.setVisibility(View.VISIBLE);
            String title = intent.getStringExtra(EXTRA_TITLE);
            String description = intent.getStringExtra(EXTRA_DESCRIPTION);
            String date = intent.getStringExtra(EXTRA_DATE);
            String time = intent.getStringExtra(EXTRA_TIME);
            textViewDescViewNote.setText(title);
            textViewViewNote.setText(description);
            textViewDateShow.setText("Задача запланирована на: " + date + " " + time);
            //Log.d("time", time);
            textViewDescViewNote.setTextSize(35f);
            textViewViewNote.setTextSize(20f);
            textViewDateShow.setTextSize(20f);
            textViewDescViewNote.setTextColor(Color.BLACK);
            textViewViewNote.setTextColor(Color.BLACK);
            textViewDateShow.setTextColor(Color.BLACK);
            calendarView.setVisibility(View.GONE);
        } else if (intent.hasExtra(EXTRA_ID)) {
            setTitle(getString(R.string.edit_note));
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            String time = intent.getStringExtra(EXTRA_TIME);
            textViewDateShow.setText(intent.getStringExtra(EXTRA_DATE) + " " + time);
        } else {
            setTitle(getString(R.string.add_note));
        }

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (!getSupportActionBar().getTitle().toString().equals(getString(R.string.add_note))) {
            menuInflater.inflate(R.menu.add_note_to_do_menu, menu);
        } else if (!getSupportActionBar().getTitle().toString().equals(getString(R.string.edit_note))) {
            menuInflater.inflate(R.menu.add_note_to_do_menu, menu);
        } else {
            menuInflater.inflate(R.menu.add_task_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String priority = sharedPreferences.getString("priority", "not so important");
        String date = sharedPreferences.getString("dateNew", "date not selected");
        String time = sharedPreferences.getString("remindTime", "");
        Log.d("time", time);

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_edit_text_toast), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_TIME, time);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    public void notifyDate(View view) {
        Calendar calendar = Calendar.getInstance();

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                editor.putString("remindTime", time);
                editor.apply();
                Log.d("time", time);
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
        tpd.show();
    }

}
