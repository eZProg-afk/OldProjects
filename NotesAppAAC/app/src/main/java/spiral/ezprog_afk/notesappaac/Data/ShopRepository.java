package spiral.ezprog_afk.notesappaac.Data;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import spiral.ezprog_afk.notesappaac.Models.ShopItem;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;

public class ShopRepository {

    //Vars:

    private ShopDAO shopDAO;
    private LiveData<List<ShopItem>> allShopItems;

    public ShopRepository(Application application) {
        ShopDatabase shopDatabase = ShopDatabase.getInstance(application);
        shopDAO = shopDatabase.getShopDAO();
        allShopItems = shopDAO.getAllShopItems();
    }

    public void insert(ShopItem item) {
        new ShopRepository.InsertShopAsyncTask(shopDAO).execute(item);
    }

    public void update(ShopItem item) {
        new ShopRepository.UpdateShopAsyncTask(shopDAO).execute(item);
    }

    public void delete(ShopItem item) {
        new ShopRepository.DeleteShopAsyncTask(shopDAO).execute(item);
    }

    public void deleteAllShopItems() {
        new ShopRepository.DeleteAllShopAsyncTask(shopDAO).execute();
    }

    public LiveData<List<ShopItem>> getAllShopItems() {
        return allShopItems;
    }

    private static class InsertShopAsyncTask extends AsyncTask<ShopItem, Void, Void> {

        private ShopDAO shopDAO;

        private InsertShopAsyncTask(ShopDAO shopDAO) {
            this.shopDAO = shopDAO;
        }

        @Override
        protected Void doInBackground(ShopItem... shopItems) {
            shopDAO.insertShopItem(shopItems[0]);
            return null;
        }
    }

    private static class UpdateShopAsyncTask extends AsyncTask<ShopItem, Void, Void> {

        private ShopDAO shopDAO;

        private UpdateShopAsyncTask(ShopDAO shopDAO) {
            this.shopDAO = shopDAO;
        }

        @Override
        protected Void doInBackground(ShopItem... shopItems) {
            shopDAO.updateShopItem(shopItems[0]);
            return null;
        }
    }

    private static class DeleteShopAsyncTask extends AsyncTask<ShopItem, Void, Void> {

        private ShopDAO shopDAO;

        private DeleteShopAsyncTask(ShopDAO shopDAO) {
            this.shopDAO = shopDAO;
        }

        @Override
        protected Void doInBackground(ShopItem... shopItems) {
            shopDAO.deleteShopItem(shopItems[0]);
            return null;
        }
    }

    private static class DeleteAllShopAsyncTask extends AsyncTask<Void, Void, Void> {

        private ShopDAO shopDAO;

        private DeleteAllShopAsyncTask(ShopDAO shopDAO) {
            this.shopDAO = shopDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shopDAO.deleteAllShopItems();
            return null;
        }
    }

}
