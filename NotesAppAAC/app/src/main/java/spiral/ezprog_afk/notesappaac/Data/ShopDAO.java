package spiral.ezprog_afk.notesappaac.Data;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import spiral.ezprog_afk.notesappaac.Models.ShopItem;

@Dao
public interface ShopDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShopItem(ShopItem item);

    @Update
    void updateShopItem(ShopItem item);

    @Delete
    void deleteShopItem(ShopItem item);

    @Query("DELETE FROM shop_items")
    void deleteAllShopItems();

    @Query("SELECT * FROM shop_items")
    LiveData<List<ShopItem>> getAllShopItems();

    @Query("SELECT * FROM shop_items WHERE id == :id")
    ShopItem getShopItemById(int id);
}
