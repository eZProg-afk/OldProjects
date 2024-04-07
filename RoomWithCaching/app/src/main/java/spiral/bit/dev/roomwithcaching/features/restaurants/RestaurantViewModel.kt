package spiral.bit.dev.roomwithcaching.features.restaurants

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import spiral.bit.dev.roomwithcaching.api.RestaurantApi
import spiral.bit.dev.roomwithcaching.data.Restaurant
import spiral.bit.dev.roomwithcaching.data.RestaurantRepository
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    repository: RestaurantRepository
) : ViewModel(), LifecycleObserver {

    val restaurants = repository.getRestaurants().asLiveData()
}