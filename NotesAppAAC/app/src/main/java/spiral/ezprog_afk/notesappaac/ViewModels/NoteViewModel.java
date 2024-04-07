package spiral.ezprog_afk.notesappaac.ViewModels;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Data.NoteRepository;
import spiral.ezprog_afk.notesappaac.Models.Note;

public class NoteViewModel extends AndroidViewModel {

    //Vars:

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
