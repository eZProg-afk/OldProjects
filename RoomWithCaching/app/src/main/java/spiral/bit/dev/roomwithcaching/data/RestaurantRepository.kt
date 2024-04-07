package spiral.bit.dev.roomwithcaching.data

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import spiral.bit.dev.roomwithcaching.api.RestaurantApi
import spiral.bit.dev.roomwithcaching.util.networkBoundResource
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val restaurantApi: RestaurantApi,
    private val db: RestaurantDatabase
) {
    private val restaurantDao = db.restaurantDao()

    fun getRestaurants() = networkBoundResource(
        query = {
            restaurantDao.getAllRestaurants()
        },
        fetch = {
            delay(2000)
            restaurantApi.getRestaurants()
        },
        saveFetchResult = { restaurants ->
            db.withTransaction {
                restaurantDao.deleteAllRestaurants()
                restaurantDao.insertList(restaurants)
            }
        }
    )
}