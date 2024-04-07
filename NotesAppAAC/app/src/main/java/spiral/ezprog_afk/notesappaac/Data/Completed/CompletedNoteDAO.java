package spiral.ezprog_afk.notesappaac.Data.Completed;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Models.Completed.CompletedNote;
import spiral.ezprog_afk.notesappaac.Models.Note;

@Dao
public interface CompletedNoteDAO {

    @Insert
    void insertCompleted(CompletedNote completedNote);

    @Delete
    void deleteCompleted(CompletedNote completedNote);

    @Update
    void updateCompleted(CompletedNote completedNote);

    @Query("DELETE FROM completed_note_table")
    void deleteAllCompletedNotes();

    @Query("SELECT * FROM completed_note_table ORDER BY priority DESC")
    LiveData<List<CompletedNote>> getAllCompletedNotes();

}
