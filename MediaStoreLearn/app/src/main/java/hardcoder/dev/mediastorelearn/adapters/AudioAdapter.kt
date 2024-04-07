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
import hardcoder.dev.mediastorelearn.data.Audio

class AudioAdapter(private val onClick: (Audio) -> Unit) : ListAdapter<Audio, AudioAdapter.AudioViewHolder>(Diff()) {

    class Diff : DiffUtil.ItemCallback<Audio>() {
        override fun areItemsTheSame(oldItem: Audio, newItem: Audio) = oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Audio, newItem: Audio) = oldItem == newItem
    }

    inner class AudioViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.displayNameTextView)
        val durationTextView = itemView.findViewById<TextView>(R.id.durationTextView)
        val sizeTextView = itemView.findViewById<TextView>(R.id.sizeTextView)
        val albumTextView = itemView.findViewById<TextView>(R.id.albumTextView)
        val authorOrComposerTextView = itemView.findViewById<TextView>(R.id.authorOrComposerTextView)

        val thumbnailImageView = itemView.findViewById<ImageView>(R.id.thumbnailImageView)
        val rootConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.root)

        fun bind(audio: Audio) {
            nameTextView.text = "Audio name - ${audio.name}"
            durationTextView.text = "Audio Duration - ${audio.duration}"
            sizeTextView.text = "Audio size - ${audio.size}"
            albumTextView.text = "Audio album - ${audio.album}"
            authorOrComposerTextView.text = "Audio author or composer - ${audio.authorOrComposer}"

            //thumbnailImageView.setImageBitmap(audio.thumbnail)

            rootConstraintLayout.setOnClickListener {
                onClick(audio)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        return AudioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}