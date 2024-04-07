package spiral.ezprog_afk.notesappaac.ViewModels;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import spiral.ezprog_afk.notesappaac.Data.ShopRepository;
import spiral.ezprog_afk.notesappaac.Models.ShopItem;

public class ShopViewModel extends AndroidViewModel {

    //Vars:

    private ShopRepository repository;
    private LiveData<List<ShopItem>> allShopItems;

    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new ShopRepository(application);
        allShopItems = repository.getAllShopItems();
    }

    public void insert(ShopItem shopItem) {
        repository.insert(shopItem);
    }

    public void update(ShopItem shopItem) {
        repository.update(shopItem);
    }

    public void delete(ShopItem shopItem) {
        repository.delete(shopItem);
    }

    public void deleteAllShopItems() {
        repository.deleteAllShopItems();
    }

    public LiveData<List<ShopItem>> getAllShopItems() {
        return allShopItems;
    }
}
