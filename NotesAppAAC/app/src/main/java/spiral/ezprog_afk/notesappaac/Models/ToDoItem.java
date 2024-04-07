package spiral.ezprog_afk.notesappaac.Models;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "to_do_items")
public class ToDoItem {

    //Vars:

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private int priority;

    @Ignore
    public ToDoItem() {
    }

    public ToDoItem(String text, int priority) {
        this.text = text;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
