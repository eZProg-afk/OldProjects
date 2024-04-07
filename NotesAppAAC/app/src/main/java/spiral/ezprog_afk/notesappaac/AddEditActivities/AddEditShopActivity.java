package spiral.ezprog_afk.notesappaac.AddEditActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import spiral.ezprog_afk.notesappaac.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.Date;

public class AddEditShopActivity extends AppCompatActivity {

    //Vars:

    public static final String EXTRA_SHOP_ID =
            "spiral.ezprog_afk.notesappaac.EXTRA_ID";
    public static final String EXTRA_SHOP_NAME =
            "spiral.ezprog_afk.notesappaac.EXTRA_TITLE";


    private EditText nameEditText;
    private PublisherAdView mPublisherAdView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shop);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        nameEditText = findViewById(R.id.edit_text_title);

        mPublisherAdView = findViewById(R.id.publisherAdViewNote);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_SHOP_ID)) {
            setTitle(getString(R.string.edit_purchase));
            nameEditText.setText(intent.getStringExtra(EXTRA_SHOP_NAME));
        } else {
            setTitle(getString(R.string.add_purchase));
        }

//        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
//        mPublisherAdView.loadAd(adRequest);
//
//        mPublisherAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//            }
//
//            @Override
//            public void onAdOpened() {
//            }
//
//            @Override
//            public void onAdClicked() {
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//        });
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
                savePurchase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePurchase() {
        String name = nameEditText.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_purchase_edit_text), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_SHOP_NAME, name);

        int id = getIntent().getIntExtra(EXTRA_SHOP_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_SHOP_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}