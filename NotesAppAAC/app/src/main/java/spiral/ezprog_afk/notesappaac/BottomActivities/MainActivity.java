package spiral.ezprog_afk.notesappaac.BottomActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import spiral.ezprog_afk.notesappaac.Adapters.NoteAdapter;
import spiral.ezprog_afk.notesappaac.AddEditActivities.AddEditNoteActivity;
import spiral.ezprog_afk.notesappaac.Other.BaseActivity;
import spiral.ezprog_afk.notesappaac.Models.Note;
import spiral.ezprog_afk.notesappaac.Other.MyAlarmReceiver;
import spiral.ezprog_afk.notesappaac.Other.SplashScreen;
import spiral.ezprog_afk.notesappaac.ViewModels.NoteViewModel;
import spiral.ezprog_afk.notesappaac.R;
import spiral.ezprog_afk.notesappaac.Settings.SettingsActivity;

import static spiral.ezprog_afk.notesappaac.Other.MyAlarmReceiver.CHANNEL_ID;

public class MainActivity extends BaseActivity {

    //Vars and Const:

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int VIEW_NOTE_REQUEST = 3;
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton addTaskBtn;
    private NoteAdapter adapter;
    private TextView textViewHint;
    public int completedNotesCount;
    private boolean hasVisited;
    private SharedPreferences sd;
    private SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomNav();
        prepareApp();

        sd = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sd.edit();


        hasVisited = sd.getBoolean("hasVisit", false);

        if (hasVisited) {

        } else if (sd.getBoolean("splash_screen", true)) {
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            editor.putBoolean("hasVisit", true);
            editor.apply();
            startActivity(intent);
        }

        addTaskBtn = findViewById(R.id.button_add_note);
        textViewHint = findViewById(R.id.tv_hint_while_empty_recycler);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(noteIntent, ADD_NOTE_REQUEST);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.complete)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                                completedNotesCount += 1;
                                getPreferences();
                                Toast.makeText(MainActivity.this, getString(R.string.deleted_note), Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.completing_of_task));
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Note note) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.wdywtd)
                        .setCancelable(false)
                        .setPositiveButton(R.string.view_a_note, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                                intent.putExtra(AddEditNoteActivity.EXTRA_DATE, note.getDateOfTask());
                                editor.putBoolean("isView", true);
                                editor.apply();
                                startActivityForResult(intent, VIEW_NOTE_REQUEST);
                            }
                        }).setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                        intent.putExtra(AddEditNoteActivity.EXTRA_DATE, note.getDateOfTask());
                        startActivityForResult(intent, EDIT_NOTE_REQUEST);
                    }
                }).setNeutralButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.note));
                alert.show();
            }
        });
    }

    private void prepareApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "work_note";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.MINUTE, 3);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 2000, pendingIntent);
    }


    public void getPreferences() {
        editor.putInt("countOfCompletedNotes", completedNotesCount);
        editor.apply();
        if (sd.getBoolean("enable_sound", true)) {
            String nameOfMelody = sd.getString("timer_melody", "note_complete");
            if (nameOfMelody.equals("note_complete")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.note_complete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_2")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.sent_note_complete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_3")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.note_delete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_4")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        R.raw.note_star_delete);
                mediaPlayer.start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddEditNoteActivity.EXTRA_TIME);

            Note note = new Note(title, description, priority, date, time);
            noteViewModel.insert(note);
            getPreferences();
            Toast.makeText(this, R.string.saved_note, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, R.string.not_update_note, Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddEditNoteActivity.EXTRA_TIME);

            Note note = new Note(title, description, priority, date, time);
            note.setId(id);
            noteViewModel.update(note);
            getPreferences();
            Toast.makeText(this, getString(R.string.update_note), Toast.LENGTH_SHORT).show();
        } else if (requestCode == VIEW_NOTE_REQUEST && resultCode == RESULT_OK) {

        } else {
            Toast.makeText(this, R.string.not_saved_note, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.aystdat)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (noteViewModel != null) {
                                    noteViewModel.deleteAllNotes();
                                    getPreferences();
                                    Toast.makeText(MainActivity.this, getString(R.string.deleted_all_notes), Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.deleting_of_all_tasks));
                alert.show();
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_down:
                if (BaseActivity.getBottomNav().getVisibility() == View.VISIBLE) {
                    hideBottomNav();
                } else {
                    unHideBottomNav();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
