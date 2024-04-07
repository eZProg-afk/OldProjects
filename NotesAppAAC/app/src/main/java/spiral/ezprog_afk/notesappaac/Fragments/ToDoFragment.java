package spiral.ezprog_afk.notesappaac.Fragments;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import spiral.ezprog_afk.notesappaac.Adapters.ToDoAdapter;
import spiral.ezprog_afk.notesappaac.AddEditActivities.AddEditToDoActivity;
import spiral.ezprog_afk.notesappaac.BottomActivities.MainActivity;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;
import spiral.ezprog_afk.notesappaac.R;
import spiral.ezprog_afk.notesappaac.Settings.SettingsActivity;
import spiral.ezprog_afk.notesappaac.ViewModels.ToDoViewModel;

import static android.app.Activity.RESULT_OK;

public class ToDoFragment extends Fragment {

    //Vars:

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int ADD_TO_DO_REQUEST = 1;
    public static final int EDIT_TO_DO_REQUEST = 2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FloatingActionButton addToDoBtn;
    private RecyclerView recyclerView;
    private ToDoViewModel toDoViewModel;
    private CheckBox checkBoxComplete;
    private int completedTODOsCount = 0;

    private String mParam1;
    private String mParam2;

    public ToDoFragment() {
    }

    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void getPreferences() {
        editor.putInt("countOfCompletedTODOs", completedTODOsCount);
        editor.apply();
        if (sharedPreferences.getBoolean("enable_sound", true)) {
            String nameOfMelody = sharedPreferences.getString("timer_melody", "note_complete");
            if (nameOfMelody.equals("note_complete")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),
                        R.raw.note_complete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_2")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),
                        R.raw.sent_note_complete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_3")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),
                        R.raw.note_delete);
                mediaPlayer.start();
            } else if (nameOfMelody.equals("sound_4")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),
                        R.raw.note_star_delete);
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TO_DO_REQUEST && resultCode == RESULT_OK) {
            String text = data.getStringExtra(AddEditToDoActivity.EXTRA_TO_DO_TEXT);
            int priority = data.getIntExtra(AddEditToDoActivity.EXTRA_TO_DO_PRIORITY, 1);

            ToDoItem item = new ToDoItem(text, priority);
            toDoViewModel.insert(item);

            Toast.makeText(getContext(), R.string.saved_note, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TO_DO_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditToDoActivity.EXTRA_TO_DO_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), R.string.not_update_to_do, Toast.LENGTH_SHORT).show();
            }

            String text = data.getStringExtra(AddEditToDoActivity.EXTRA_TO_DO_TEXT);
            int priority = data.getIntExtra(AddEditToDoActivity.EXTRA_TO_DO_PRIORITY, 1);

            ToDoItem item = new ToDoItem(text, priority);
            item.setId(id);
            toDoViewModel.update(item);
            Toast.makeText(getContext(), getString(R.string.update_to_do), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.not_saved_to_do, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Вы точно хотите удалить все задачи?")
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toDoViewModel.deleteAllToDoItems();
                                getPreferences();
                                Toast.makeText(getContext(), getString(R.string.deleted_all_notes), Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("Удаление всех задач");
                alert.show();
                return true;
            case R.id.settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_to_do, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        addToDoBtn = view.findViewById(R.id.button_add_to_do);

        addToDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddEditIntent = new Intent(getContext(), AddEditToDoActivity.class);
                startActivityForResult(toAddEditIntent, ADD_TO_DO_REQUEST);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_todo);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ToDoAdapter adapter = new ToDoAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        toDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDoItems().observe(getViewLifecycleOwner(), new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {
                adapter.submitList(toDoItems);
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
                checkBoxComplete = view.findViewById(R.id.complete_to_do_item_chx);
                if (checkBoxComplete.isChecked()) {
                    completedTODOsCount++;
                    getPreferences();
                    toDoViewModel.delete(adapter.getToDoItemAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(getContext(), getString(R.string.deleted_to_do), Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.complete)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    toDoViewModel.delete(adapter.getToDoItemAt(viewHolder.getAdapterPosition()));
                                    completedTODOsCount += 1;
                                    getPreferences();
                                    Toast.makeText(getContext(), getString(R.string.deleted_to_do), Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setTitle(R.string.completing_of_to_do);
                    alert.show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToDoItem item) {
                Intent intent = new Intent(getContext(), AddEditToDoActivity.class);
                intent.putExtra(AddEditToDoActivity.EXTRA_TO_DO_ID, item.getId());
                intent.putExtra(AddEditToDoActivity.EXTRA_TO_DO_TEXT, item.getText());
                startActivityForResult(intent, EDIT_TO_DO_REQUEST);
            }
        });
        return view;
    }
}