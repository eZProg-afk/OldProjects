package spiral.ezprog_afk.notesappaac.Data;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

public class ToDoRepository {

    //Vars:

    private ToDoDAO toDoDAO;
    private LiveData<List<ToDoItem>> allToDoItems;

    public ToDoRepository(Application application) {
        ToDoDatabase toDoDatabase = ToDoDatabase.getInstance(application);
        toDoDAO = toDoDatabase.getToDoDAO();
        allToDoItems = toDoDAO.getAllToDoItems();
    }

    public void insert(ToDoItem item) {
        new ToDoRepository.InsertToDoAsyncTask(toDoDAO).execute(item);
    }

    public void update(ToDoItem item) {
        new ToDoRepository.UpdateToDoAsyncTask(toDoDAO).execute(item);
    }

    public void delete(ToDoItem item) {
        new ToDoRepository.DeleteToDoAsyncTask(toDoDAO).execute(item);
    }

    public void deleteAllToDoItems() {
        new ToDoRepository.DeleteAllToDoAsyncTask(toDoDAO).execute();
    }

    public LiveData<List<ToDoItem>> getAllToDoItems() {
        return allToDoItems;
    }

    private static class InsertToDoAsyncTask extends AsyncTask<ToDoItem, Void, Void> {

        private ToDoDAO toDoDAO;

        private InsertToDoAsyncTask(ToDoDAO toDoDAO) {
            this.toDoDAO = toDoDAO;
        }

        @Override
        protected Void doInBackground(ToDoItem... toDoItems) {
            toDoDAO.insertToDoItem(toDoItems[0]);
            return null;
        }
    }

    private static class UpdateToDoAsyncTask extends AsyncTask<ToDoItem, Void, Void> {

        private ToDoDAO toDoDAO;

        private UpdateToDoAsyncTask(ToDoDAO toDoDao) {
            this.toDoDAO = toDoDao;
        }

        @Override
        protected Void doInBackground(ToDoItem... toDoItems) {
            toDoDAO.updateToDoItem(toDoItems[0]);
            return null;
        }
    }

    private static class DeleteToDoAsyncTask extends AsyncTask<ToDoItem, Void, Void> {

        private ToDoDAO toDoDAO;

        private DeleteToDoAsyncTask(ToDoDAO toDoDAO) {
            this.toDoDAO = toDoDAO;
        }

        @Override
        protected Void doInBackground(ToDoItem... toDoItems) {
            toDoDAO.deleteToDoItem(toDoItems[0]);
            return null;
        }
    }

    private static class DeleteAllToDoAsyncTask extends AsyncTask<Void, Void, Void> {

        private ToDoDAO toDoDAO;

        private DeleteAllToDoAsyncTask(ToDoDAO toDoDAO) {
            this.toDoDAO = toDoDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            toDoDAO.deleteAllToDoItems();
            return null;
        }
    }

}
