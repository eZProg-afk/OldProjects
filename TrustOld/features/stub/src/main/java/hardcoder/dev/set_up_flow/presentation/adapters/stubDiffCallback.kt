package hardcoder.dev.set_up_flow.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

val stubDiffCallback = object : DiffUtil.ItemCallback<ItemStub>() {
    override fun areItemsTheSame(oldItem: ItemStub, newItem: ItemStub) = oldItem.previewThumbnailResourceId == newItem.previewThumbnailResourceId

    override fun areContentsTheSame(oldItem: ItemStub, newItem: ItemStub) = oldItem == newItem
}