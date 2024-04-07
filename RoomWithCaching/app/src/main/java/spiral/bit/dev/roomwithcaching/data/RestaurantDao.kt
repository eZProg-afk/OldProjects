package spiral.bit.dev.roomwithcaching.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(restaurants: List<Restaurant>)

    @Query("DELETE FROM restaurants_table")
    suspend fun deleteAllRestaurants()

    @Query("SELECT * FROM restaurants_table")
    fun getAllRestaurants(): Flow<List<Restaurant>>
}