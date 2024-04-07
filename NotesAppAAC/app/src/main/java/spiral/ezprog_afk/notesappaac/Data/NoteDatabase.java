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
import spiral.ezprog_afk.notesappaac.Models.Note;
import spiral.ezprog_afk.notesappaac.R;

@Database(entities = Note.class, version = 8)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDAO getNoteDAO();

    //Vars:

    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
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

        private NoteDAO noteDAO;

        private PopulateDBAsyncTask(NoteDatabase db) {
            noteDAO = db.getNoteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
