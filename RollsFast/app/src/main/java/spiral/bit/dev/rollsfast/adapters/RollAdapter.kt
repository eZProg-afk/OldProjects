package spiral.bit.dev.rollsfast.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import spiral.bit.dev.rollsfast.R
import spiral.bit.dev.rollsfast.model.Product
import spiral.bit.dev.rollsfast.other.REFERENCE_STORAGE
import spiral.bit.dev.rollsfast.other.downloadAndSetImage
import spiral.bit.dev.rollsfast.other.getURL

class RollAdapter(var rollsList: ArrayList<Product>) :
    RecyclerView.Adapter<RollAdapter.RollViewHolder>() {

    inner class RollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rollName: TextView = itemView.findViewById(R.id.roll_name)
        val rollPrice: TextView = itemView.findViewById(R.id.roll_price)
        val rollImage: ImageView = itemView.findViewById(R.id.roll_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollViewHolder {
        return RollViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.roll_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RollViewHolder, position: Int) {
        val roll = rollsList[position]
        holder.rollName.text = roll.rollName
        holder.rollPrice.text = roll.rollPrice
        val path = REFERENCE_STORAGE.child("products").child(roll.rollId)
        getURL(path) { holder.rollImage.downloadAndSetImage(it) }
    }

    override fun getItemCount(): Int = rollsList.size

    fun submitList(rollsList: ArrayList<Product>) {
        this.rollsList = rollsList
        notifyDataSetChanged()
    }
}