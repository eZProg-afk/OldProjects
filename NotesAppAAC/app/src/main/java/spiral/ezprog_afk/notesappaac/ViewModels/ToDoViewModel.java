package spiral.ezprog_afk.notesappaac.ViewModels;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Data.ToDoRepository;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

public class ToDoViewModel extends AndroidViewModel {

    //Vars:

    private ToDoRepository repository;
    private LiveData<List<ToDoItem>> allToDoItems;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        allToDoItems = repository.getAllToDoItems();
    }

    public void insert(ToDoItem toDoItem) {
        repository.insert(toDoItem);
    }

    public void update(ToDoItem toDoItem) {
        repository.update(toDoItem);
    }

    public void delete(ToDoItem toDoItem) {
        repository.delete(toDoItem);
    }

    public void deleteAllToDoItems() {
        repository.deleteAllToDoItems();
    }

    public LiveData<List<ToDoItem>> getAllToDoItems() {
        return allToDoItems;
    }
}
