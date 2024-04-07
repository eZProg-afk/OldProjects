package spiral.bit.dev.roomwithcaching.api

import retrofit2.http.GET
import spiral.bit.dev.roomwithcaching.data.Restaurant

interface RestaurantApi {

    companion object {
        const val BASE_URL = "https://random-data-api.com/api/"
    }

    @GET("restaurant/random_restaurant?size=20")
    suspend fun getRestaurants(): List<Restaurant>
}