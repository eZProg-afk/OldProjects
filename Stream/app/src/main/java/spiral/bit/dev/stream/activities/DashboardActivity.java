package spiral.bit.dev.stream.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import spiral.bit.dev.stream.R;
import spiral.bit.dev.stream.other.BaseActivity;

public class DashboardActivity extends BaseActivity {

    private EditText codeEditText;
    private SharedPreferences prefCall;
    private SharedPreferences.Editor editorCall;
    private FirebaseAuth auth;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle(auth.getCurrentUser().getEmail());
        setUpBottomNavView();

        prefCall = getSharedPreferences("call_prefs", 0);
        editorCall = prefCall.edit();

        codeEditText = findViewById(R.id.et_enter_code);
        SwitchMaterial microOff = findViewById(R.id.micro_off_switch);
        SwitchMaterial cameraOff = findViewById(R.id.camera_off_switch);
        microOff.setOnClickListener(v -> {
            if (v.isSelected()) {
                editorCall.putBoolean("micro", false);
            } else {
                editorCall.putBoolean("micro", true);
            }
            editorCall.apply();
        });

        cameraOff.setOnClickListener(v -> {
            if (v.isSelected()) {
                editorCall.putBoolean("camera", false);
            } else {
                editorCall.putBoolean("camera", true);
            }
            editorCall.apply();
        });

        codeEditText.requestFocus();
        Button joinBtn = findViewById(R.id.btn_join);
        Button shareBtn = findViewById(R.id.btn_share);

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        joinBtn.setOnClickListener(v -> {
            JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
            userInfo.setDisplayName(auth.getCurrentUser().getDisplayName());
            userInfo.setEmail(auth.getCurrentUser().getEmail());
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions
                    .Builder().setRoom(codeEditText.getText().toString())
                    .setWelcomePageEnabled(false)
                    .setAudioMuted(prefCall.getBoolean("micro", false))
                    .setVideoMuted(prefCall.getBoolean("camera", false))
                    .setUserInfo(userInfo)
                    .build();
            JitsiMeetActivity.launch(DashboardActivity.this, options);
        });


        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            final String appPackageName = getPackageName();
            String shareBody = "Присоединиться к моей конференции в приложении STREAM можно по коду \"" + codeEditText.
                    getText().toString() + "\"" + "\n " + " Приложение можно скачать по ссылке " + "market://details?id=" +
                    appPackageName;
            intent.setType("image/jpg");
            Bitmap bitmap = ((BitmapDrawable) getDrawable(R.drawable.ava_stream)).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    bitmap, "image", null);
            Uri imageDrawUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, imageDrawUri);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, codeEditText.getText().toString());
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Поделиться кодом"));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out_item) {
            auth.signOut();
            Intent logOutIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logOutIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}