package spiral.bit.dev.roomwithcaching.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao
}