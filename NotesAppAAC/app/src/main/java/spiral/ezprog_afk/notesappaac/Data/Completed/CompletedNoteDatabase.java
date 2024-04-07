package spiral.ezprog_afk.notesappaac.Data.Completed;


//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;

@Database(entities = CompletedNote.class, version = 1)
public abstract class CompletedNoteDatabase extends RoomDatabase {
    public abstract CompletedNoteDAO getCompletedNoteDAO();

    //Vars:

    private static CompletedNoteDatabase instance;

    public static synchronized CompletedNoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CompletedNoteDatabase.class,
                    "completed_note_database")
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

        private CompletedNoteDAO completedNoteDAO;

        private PopulateDBAsyncTask(CompletedNoteDatabase db) {
            completedNoteDAO = db.getCompletedNoteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
