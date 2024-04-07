package spiral.ezprog_afk.notesappaac.Data;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import spiral.ezprog_afk.notesappaac.Models.ShopItem;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

@Database(entities = ShopItem.class, version = 2)

public abstract class ShopDatabase extends RoomDatabase {

    public abstract ShopDAO getShopDAO();

    //Vars:

    private static ShopDatabase instance;

    public static synchronized ShopDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ShopDatabase.class, "shopItemsDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private ShopDAO shopDAO;

        private PopulateDBAsyncTask(ShopDatabase db) {
            shopDAO = db.getShopDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
