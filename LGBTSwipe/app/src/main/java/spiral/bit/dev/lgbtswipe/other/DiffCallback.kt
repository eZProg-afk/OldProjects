package spiral.bit.dev.lgbtswipe.other

import androidx.recyclerview.widget.DiffUtil
import spiral.bit.dev.lgbtswipe.models.Model

class DiffCallback(
    private val oldList: List<Model>,
    private val newList: List<Model>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].time_stamp_moon == newList[newItemPosition].time_stamp_moon

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}