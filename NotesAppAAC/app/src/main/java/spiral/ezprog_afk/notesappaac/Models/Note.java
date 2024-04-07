package spiral.ezprog_afk.notesappaac.Models;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "note_table")
public class Note {

    //Vars:

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String priority;
    private String dateOfTask;
    private String timeOfTask;

    public Note(String title, String description, String priority, String dateOfTask, String timeOfTask) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateOfTask = dateOfTask;
        this.timeOfTask = timeOfTask;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public String getDateOfTask() {
        return dateOfTask;
    }

    public String getTimeOfTask() {
        return timeOfTask;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
