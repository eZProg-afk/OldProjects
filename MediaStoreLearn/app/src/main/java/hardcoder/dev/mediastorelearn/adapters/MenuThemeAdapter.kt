package hardcoder.dev.mediastorelearn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hardcoder.dev.mediastorelearn.R
import hardcoder.dev.mediastorelearn.data.Image
import hardcoder.dev.mediastorelearn.data.MenuTheme
import hardcoder.dev.mediastorelearn.data.Video

class MenuThemeAdapter(private val onClick: (MenuTheme) -> Unit) : ListAdapter<MenuTheme, MenuThemeAdapter.MenuThemeViewHolder>(Diff()) {

    private lateinit var context: Context

    class Diff : DiffUtil.ItemCallback<MenuTheme>() {
        override fun areItemsTheSame(oldItem: MenuTheme, newItem: MenuTheme) = oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: MenuTheme, newItem: MenuTheme) = oldItem == newItem
    }

    inner class MenuThemeViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val rootConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.rootConstraintLayout)

        fun bind(menuTheme: MenuTheme) {
            titleTextView.text = menuTheme.title
            val drawable = ResourcesCompat.getDrawable(context.resources, menuTheme.drawableEndResourceId, context.theme)
            titleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )

            rootConstraintLayout.setOnClickListener {
                onClick(menuTheme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuThemeViewHolder {
        context = parent.context
        return MenuThemeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_menu_theme, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuThemeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}