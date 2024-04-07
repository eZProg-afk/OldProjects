package spiral.ezprog_afk.notesappaac.ViewModels.Completed;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Data.Completed.CompletedNoteRepository;
import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;

public class CompletedNoteViewModel extends AndroidViewModel {

    //Vars:

    private CompletedNoteRepository completedNoteRepository;
    private LiveData<List<CompletedNote>> allCompletedNotes;

    public CompletedNoteViewModel(@NonNull Application application) {
        super(application);
        completedNoteRepository = new CompletedNoteRepository(application);
        allCompletedNotes = completedNoteRepository.getAllCompletedNotes();
    }

    public void insertCompleted(CompletedNote completedNote) {
        completedNoteRepository.insertCompleted(completedNote);
    }

    public void updateCompleted(CompletedNote completedNote) {
        completedNoteRepository.updateCompleted(completedNote);
    }

    public void deleteCompleted(CompletedNote completedNote) {
        completedNoteRepository.deleteCompleted(completedNote);
    }

    public void deleteAllCompletedNotes() {
        completedNoteRepository.deleteAllCompletedNotes();
    }

    public LiveData<List<CompletedNote>> getAllCompletedNotes() {
        return allCompletedNotes;
    }
}
