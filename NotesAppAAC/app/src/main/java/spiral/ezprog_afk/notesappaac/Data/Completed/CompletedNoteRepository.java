package spiral.ezprog_afk.notesappaac.Data.Completed;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;

public class CompletedNoteRepository {

    //Vars:

    private CompletedNoteDAO completedNoteDAO;
    private LiveData<List<CompletedNote>> allCompletedNotes;

    public CompletedNoteRepository(Application application) {
        CompletedNoteDatabase completedNoteDatabase = CompletedNoteDatabase.getInstance(application);
        completedNoteDAO = completedNoteDatabase.getCompletedNoteDAO();
        allCompletedNotes = completedNoteDAO.getAllCompletedNotes();
    }

    public void insertCompleted(CompletedNote completedNote) {
        new InsertCompletedNoteAsyncTask(completedNoteDAO).execute(completedNote);
    }

    public void updateCompleted(CompletedNote completedNote) {
        new UpdateCompletedNoteAsyncTask(completedNoteDAO).execute(completedNote);
    }

    public void deleteCompleted(CompletedNote completedNote) {
        new DeleteCompletedNoteAsyncTask(completedNoteDAO).execute(completedNote);
    }

    public void deleteAllCompletedNotes() {
        new DeleteAllCompletedNotesNoteAsyncTask(completedNoteDAO).execute();
    }

    public LiveData<List<CompletedNote>> getAllCompletedNotes() {
        return allCompletedNotes;
    }

    private static class InsertCompletedNoteAsyncTask extends AsyncTask<CompletedNote, Void, Void> {

        private CompletedNoteDAO completedNoteDAO;

        private InsertCompletedNoteAsyncTask(CompletedNoteDAO completedNoteDAO) {
            this.completedNoteDAO = completedNoteDAO;
        }

        @Override
        protected Void doInBackground(CompletedNote... completedNotes) {
            completedNoteDAO.insertCompleted(completedNotes[0]);
            return null;
        }
    }

    private static class UpdateCompletedNoteAsyncTask extends AsyncTask<CompletedNote, Void, Void> {

        private CompletedNoteDAO completedNoteDAO;

        private UpdateCompletedNoteAsyncTask(CompletedNoteDAO completedNoteDAO) {
            this.completedNoteDAO = completedNoteDAO;
        }

        @Override
        protected Void doInBackground(CompletedNote... completedNotes) {
            completedNoteDAO.updateCompleted(completedNotes[0]);
            return null;
        }
    }

    private static class DeleteCompletedNoteAsyncTask extends AsyncTask<CompletedNote, Void, Void> {

        private CompletedNoteDAO completedNoteDAO;

        private DeleteCompletedNoteAsyncTask(CompletedNoteDAO completedNoteDAO) {
            this.completedNoteDAO = completedNoteDAO;
        }

        @Override
        protected Void doInBackground(CompletedNote... completedNotes) {
            completedNoteDAO.deleteCompleted(completedNotes[0]);
            return null;
        }
    }

    private static class DeleteAllCompletedNotesNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private CompletedNoteDAO completedNoteDAO;

        private DeleteAllCompletedNotesNoteAsyncTask(CompletedNoteDAO completedNoteDAO) {
            this.completedNoteDAO = completedNoteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            completedNoteDAO.deleteAllCompletedNotes();
            return null;
        }
    }

}
