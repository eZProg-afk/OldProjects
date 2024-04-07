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
import hardcoder.dev.mediastorelearn.data.Video

class VideoAdapter(private val onClick: (Video) -> Unit) : ListAdapter<Video, VideoAdapter.VideoViewHolder>(Diff()) {

    class Diff : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video) = oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem
    }

    inner class VideoViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.displayNameTextView)
        val durationTextView = itemView.findViewById<TextView>(R.id.durationTextView)
        val sizeTextView = itemView.findViewById<TextView>(R.id.sizeTextView)
        val thumbnailImageView = itemView.findViewById<ImageView>(R.id.thumbnailImageView)
        val rootConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.root)

        fun bind(video: Video) {
            nameTextView.text = "Video name - ${video.name}"
            durationTextView.text = "Video Duration - ${video.duration}"
            sizeTextView.text = "Video size - ${video.size}"
            thumbnailImageView.setImageBitmap(video.thumbnail)

            rootConstraintLayout.setOnClickListener {
                onClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}