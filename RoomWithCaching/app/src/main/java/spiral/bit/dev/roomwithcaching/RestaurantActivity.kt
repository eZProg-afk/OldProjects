package spiral.bit.dev.roomwithcaching

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.roomwithcaching.databinding.ActivityRestaurantBinding
import spiral.bit.dev.roomwithcaching.features.restaurants.RestaurantAdapter
import spiral.bit.dev.roomwithcaching.features.restaurants.RestaurantViewModel
import spiral.bit.dev.roomwithcaching.util.Resource

@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {

    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantAdapter = RestaurantAdapter()

        with(binding) {
            recyclerViewRestaurant.apply {
                adapter = restaurantAdapter
                setHasFixedSize(true)
            }

            viewModel.restaurants.observe(this@RestaurantActivity) { resource ->
                restaurantAdapter.submitList(resource.data)
                progressBar.isVisible =
                    resource is Resource.Loading && resource.data.isNullOrEmpty()
                textViewError.isVisible =
                    resource is Resource.Error && resource.data.isNullOrEmpty()
                textViewError.text = resource.error?.localizedMessage
            }
        }
    }
}