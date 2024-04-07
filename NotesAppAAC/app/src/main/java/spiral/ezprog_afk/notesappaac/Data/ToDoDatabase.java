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

import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

@Database(entities = ToDoItem.class, version = 2)

public abstract class ToDoDatabase extends RoomDatabase {

    public abstract ToDoDAO getToDoDAO();

    //Vars:

    private static ToDoDatabase instance;

    public static synchronized ToDoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, "toDoDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private ToDoDAO toDoDAO;

        private PopulateDBAsyncTask(ToDoDatabase db) {
            toDoDAO = db.getToDoDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
