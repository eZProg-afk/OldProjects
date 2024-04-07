package spiral.ezprog_afk.notesappaac.BottomActivities;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import spiral.ezprog_afk.notesappaac.Adapters.Completed.CompletedNoteAdapter;
import spiral.ezprog_afk.notesappaac.AddEditActivities.AddEditNoteActivity;
import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;
import spiral.ezprog_afk.notesappaac.Other.BaseActivity;
import spiral.ezprog_afk.notesappaac.R;
import spiral.ezprog_afk.notesappaac.Settings.SettingsActivity;
import spiral.ezprog_afk.notesappaac.ViewModels.Completed.CompletedNoteViewModel;

public class CompletedActivity extends BaseActivity {

    //Vars:

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 22;
    public static final int VIEW_NOTE_REQUEST = 33;
    private RecyclerView recyclerViewCompleted;
    private CompletedNoteAdapter adapter;
    private CompletedNoteViewModel completedNoteViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public int completedNotesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        setUpBottomNav();

        recyclerViewCompleted = findViewById(R.id.recycler_view_completed_notes);
        adapter = new CompletedNoteAdapter();
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompleted.setAdapter(adapter);
        recyclerViewCompleted.setHasFixedSize(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        completedNoteViewModel = ViewModelProviders.of(this).get(CompletedNoteViewModel.class);
        completedNoteViewModel.getAllCompletedNotes().observe(this, new Observer<List<CompletedNote>>() {
            @Override
            public void onChanged(List<CompletedNote> completedNotes) {
                adapter.submitList(completedNotes);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(CompletedActivity.this);
                builder.setMessage(R.string.complete)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putBoolean("visible", false);
                                editor.apply();
                                completedNoteViewModel.deleteCompleted(adapter.getCompletedNoteAt(viewHolder.getAdapterPosition()));
                                completedNotesCount += 1;
                                Toast.makeText(CompletedActivity.this, getString(R.string.deleted_note), Toast.LENGTH_SHORT).show();
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
        }).attachToRecyclerView(recyclerViewCompleted);

        adapter.setOnItemClickListener(new CompletedNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final CompletedNote completedNote){

                AlertDialog.Builder builder = new AlertDialog.Builder(CompletedActivity.this);
                builder.setMessage(R.string.wdywtd)
                        .setCancelable(false)
                        .setPositiveButton(R.string.view_a_note, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CompletedActivity.this, AddEditNoteActivity.class);
                                intent.putExtra(AddEditNoteActivity.EXTRA_ID, completedNote.getId());
                                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, completedNote.getTitle());
                                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, completedNote.getDescription());
                                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, completedNote.getPriority());
                                intent.putExtra(AddEditNoteActivity.EXTRA_DATE, completedNote.getDateOfTask());
                                editor.putBoolean("isView", true);
                                editor.apply();
                                startActivityForResult(intent, VIEW_NOTE_REQUEST);
                            }
                        }).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            String priority = data.getStringExtra("priority");
            String date = data.getStringExtra("dateOfTask");
            String time = data.getStringExtra("timeOfTask");

            CompletedNote completedNote = new CompletedNote(title, description, priority, date, time);
            completedNoteViewModel.insertCompleted(completedNote);
            Toast.makeText(CompletedActivity.this, R.string.saved_note, Toast.LENGTH_SHORT).show();
            Intent intentBack = new Intent(CompletedActivity.this, MainActivity.class);
            startActivity(intentBack);
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(CompletedActivity.this, R.string.not_update_note, Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            String priority = data.getStringExtra(AddEditNoteActivity.EXTRA_PRIORITY);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddEditNoteActivity.EXTRA_TIME);

            CompletedNote completedNote = new CompletedNote(title, description, priority, date, time);
            completedNote.setId(id);
            completedNoteViewModel.updateCompleted(completedNote);
            Toast.makeText(CompletedActivity.this, getString(R.string.update_note), Toast.LENGTH_SHORT).show();
        } else if (requestCode == VIEW_NOTE_REQUEST && resultCode == RESULT_OK) {

        } else {
            Toast.makeText(CompletedActivity.this, R.string.not_saved_note, Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.aystdat)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (completedNoteViewModel != null) {
                                    completedNoteViewModel.deleteAllCompletedNotes();
                                    Toast.makeText(CompletedActivity.this, getString(R.string.deleted_all_notes), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(CompletedActivity.this, SettingsActivity.class);
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
