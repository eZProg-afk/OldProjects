package spiral.ezprog_afk.notesappaac.Data;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Models.Note;

@Dao
public interface NoteDAO {

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();

}
