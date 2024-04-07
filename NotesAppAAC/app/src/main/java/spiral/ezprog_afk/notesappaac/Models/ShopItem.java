package spiral.ezprog_afk.notesappaac.Models;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_items")
public class ShopItem {

    //Vars:

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public ShopItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
