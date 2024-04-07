package spiral.ezprog_afk.notesappaac.AddEditActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import spiral.ezprog_afk.notesappaac.R;

public class AddEditToDoActivity extends AppCompatActivity {

    //Vars:

    public static final String EXTRA_TO_DO_ID =
            "spiral.ezprog_afk.notesappaac.EXTRA_TO_DO_ID";

    public static final String EXTRA_TO_DO_TEXT =
            "spiral.ezprog_afk.notesappaac.EXTRA_TO_DO_TEXT";

    public static final String EXTRA_TO_DO_PRIORITY =
            "spiral.ezprog_afk.notesappaac.EXTRA_TO_DO_PRIORITY";

    private EditText toDoEditText;
    private NumberPicker priorityNumberPicker;
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_to_do);

        toDoEditText = findViewById(R.id.to_do_edit_text);
        priorityNumberPicker = findViewById(R.id.number_picker_priority);
        mPublisherAdView = findViewById(R.id.publisherAdViewToDo);

        priorityNumberPicker.setMinValue(1);
        priorityNumberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        //Ad
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TO_DO_ID)) {
            setTitle(getString(R.string.edit_to_do));
            toDoEditText.setText(intent.getStringExtra(EXTRA_TO_DO_TEXT));
            priorityNumberPicker.setValue(intent.getIntExtra(EXTRA_TO_DO_PRIORITY, 1));
        } else {
            setTitle(getString(R.string.add_to_do));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_to_do_menu, menu);
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
        String to_do_text = toDoEditText.getText().toString();
        int priority = priorityNumberPicker.getValue();

        if (to_do_text.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_to_do_edit_text), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TO_DO_TEXT, to_do_text);
        data.putExtra(EXTRA_TO_DO_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_TO_DO_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_TO_DO_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
