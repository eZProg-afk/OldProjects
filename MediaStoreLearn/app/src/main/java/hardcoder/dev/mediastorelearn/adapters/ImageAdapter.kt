package hardcoder.dev.mediastorelearn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.data.Image
import hardcoder.dev.mediastorelearn.data.Video

class ImageAdapter(private val onClick: (Image) -> Unit) : ListAdapter<Image, ImageAdapter.ImageViewHolder>(Diff()) {

    class Diff : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image) = oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Image, newItem: Image) = oldItem == newItem
    }

    inner class ImageViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.displayNameTextView)
        val durationTextView = itemView.findViewById<TextView>(R.id.durationTextView)
        val sizeTextView = itemView.findViewById<TextView>(R.id.sizeTextView)
        val thumbnailImageView = itemView.findViewById<ImageView>(R.id.thumbnailImageView)
        val rootConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.root)

        fun bind(image: Image) {
            nameTextView.text = "Image name - ${image.name}"

            sizeTextView.text = "Image size - ${image.size}"
            thumbnailImageView.setImageBitmap(image.thumbnail)

            rootConstraintLayout.setOnClickListener {
                onClick(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}