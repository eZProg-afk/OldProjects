package spiral.bit.dev.roomwithcaching.features.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import spiral.bit.dev.roomwithcaching.data.Restaurant
import spiral.bit.dev.roomwithcaching.databinding.RestaurantItemBinding

class RestaurantAdapter :
    ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RestaurantViewHolder(
            RestaurantItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) =
        getItem(position).let { holder.bind(getItem(position)) }

    inner class RestaurantViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            with(binding) {
                Glide.with(itemView)
                    .load(restaurant.logo)
                    .into(imageViewLogo)
                textViewAddress.text = restaurant.address
                textViewName.text = restaurant.name
                textViewType.text = restaurant.type
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem == newItem
    }
}