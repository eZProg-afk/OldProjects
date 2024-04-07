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
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

@Dao
public interface ToDoDAO {

    @Insert
    void insertToDoItem(ToDoItem item);

    @Update
    void updateToDoItem(ToDoItem item);

    @Delete
    void deleteToDoItem(ToDoItem item);

    @Query("DELETE FROM to_do_items")
    void deleteAllToDoItems();

    @Query("SELECT * FROM to_do_items")
    LiveData<List<ToDoItem>> getAllToDoItems();
}
