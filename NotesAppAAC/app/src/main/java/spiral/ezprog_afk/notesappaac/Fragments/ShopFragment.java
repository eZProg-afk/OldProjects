package spiral.ezprog_afk.notesappaac.Fragments;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import spiral.ezprog_afk.notesappaac.Adapters.ShopAdapter;
import spiral.ezprog_afk.notesappaac.Adapters.ToDoAdapter;
import spiral.ezprog_afk.notesappaac.AddEditActivities.AddEditShopActivity;
import spiral.ezprog_afk.notesappaac.AddEditActivities.AddEditToDoActivity;
import spiral.ezprog_afk.notesappaac.Models.ShopItem;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;
import spiral.ezprog_afk.notesappaac.Other.BaseActivity;
import spiral.ezprog_afk.notesappaac.R;
import spiral.ezprog_afk.notesappaac.Settings.SettingsActivity;
import spiral.ezprog_afk.notesappaac.ViewModels.ShopViewModel;
import spiral.ezprog_afk.notesappaac.ViewModels.ToDoViewModel;

import static android.app.Activity.RESULT_OK;

public class ShopFragment extends Fragment {

    //Vars:
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int ADD_SHOP_REQUEST = 1;
    public static final int EDIT_SHOP_REQUEST = 2;
    private FloatingActionButton addShopBtn;
    private RecyclerView recyclerView;
    private ShopViewModel shopViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int completedShopItemsCount = 0;
    private String mParam1;
    private String mParam2;

    public ShopFragment() {
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SHOP_REQUEST && resultCode == RESULT_OK) {
            String text = data.getStringExtra(AddEditShopActivity.EXTRA_SHOP_NAME);

            ShopItem item = new ShopItem(text);
            shopViewModel.insert(item);

            Toast.makeText(getContext(), R.string.saved_shop, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_SHOP_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditShopActivity.EXTRA_SHOP_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), R.string.not_update_shop, Toast.LENGTH_SHORT).show();
            }

            String text = data.getStringExtra(AddEditShopActivity.EXTRA_SHOP_NAME);

            ShopItem item = new ShopItem(text);
            item.setId(id);
            shopViewModel.update(item);
            Toast.makeText(getContext(), getString(R.string.update_shop), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.not_saved_shop, Toast.LENGTH_SHORT).show();
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
                                shopViewModel.deleteAllShopItems();
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
            case R.id.menu_down:
            if (BaseActivity.getBottomNav().getVisibility() == View.VISIBLE) {
                BaseActivity.hideBottomNav();
            } else {
                BaseActivity.unHideBottomNav();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getPreferences() {
        editor.putInt("countOfCompletedShop", completedShopItemsCount);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shop, container, false);
        addShopBtn = view.findViewById(R.id.button_add_shop);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        addShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddEditIntent = new Intent(getContext(), AddEditShopActivity.class);
                startActivityForResult(toAddEditIntent, ADD_SHOP_REQUEST);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_shop);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ShopAdapter adapter = new ShopAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        shopViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        shopViewModel.getAllShopItems().observe(getViewLifecycleOwner(), new Observer<List<ShopItem>>() {
            @Override
            public void onChanged(List<ShopItem> shopItems) {
                adapter.submitList(shopItems);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.complete)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shopViewModel.delete(adapter.getShopItemAt(viewHolder.getAdapterPosition()));
                                    completedShopItemsCount += 1;
                                    getPreferences();
                                    Toast.makeText(getContext(), getString(R.string.deleted_shop), Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setTitle(R.string.completing_of_shop);
                    alert.show();
                }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShopItem item) {
                Intent intent = new Intent(getContext(), AddEditShopActivity.class);
                intent.putExtra(AddEditShopActivity.EXTRA_SHOP_ID, item.getId());
                intent.putExtra(AddEditShopActivity.EXTRA_SHOP_NAME, item.getName());
                startActivityForResult(intent, EDIT_SHOP_REQUEST);
            }
        });
        return view;
    }
}