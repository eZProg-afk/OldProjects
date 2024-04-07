package hardcoder.dev.set_up_flow.presentation.adapters

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.res.ResourcesCompat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hardcoder.dev.set_up_flow.databinding.ItemStubBinding
import hardcoder.dev.utils.common_resources.R

data class ItemStub(
    val id: Int,
    @RawRes val videoPreviewResourceId: Int,
    @DrawableRes val previewThumbnailResourceId: Int,
    var isSelected: Boolean = false
)

fun stubDelegate(onSelect: (ItemStub) -> Unit) =
    adapterDelegateViewBinding<ItemStub, ItemStub, ItemStubBinding>({ inflater, container ->
        ItemStubBinding.inflate(inflater, container, false)
    }) {
        with(binding) {
            root.setOnClickListener {
                onSelect(item)
                item.isSelected = !item.isSelected
            }

            bind {
                val stubBackgroundResourceId = if (item.isSelected) {
                    R.drawable.shape_rounded_16
                } else {
                    R.drawable.shape_border_rounded_16
                }
                val stubPreviewItemBackground = ResourcesCompat.getDrawable(
                    context.resources,
                    stubBackgroundResourceId,
                    context.theme
                )

                root.background = stubPreviewItemBackground
                stubThumbnailImageView.setImageResource(item.previewThumbnailResourceId)
            }
        }
    }