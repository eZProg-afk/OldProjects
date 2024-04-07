package spiral.bit.dev.lgbtswipe.views.main.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.SingleChatFragment

class CardStackAdapter(var list: ArrayList<User>, var context: Context) :
    RecyclerView.Adapter<CardStackAdapter.CardStackViewHolder>() {

    inner class CardStackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.avatar_user)
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val gender: TextView = itemView.findViewById(R.id.tv_gender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackViewHolder {
        return CardStackViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardStackViewHolder, position: Int) {
        val user: User = list[position]
        holder.name.text = user.name
        holder.gender.text = user.gender
        val path = user.id.let { REFERENCE_STORAGE.child(AVATARS_USERS).child(it) }
        getURL(path) { holder.image.downloadAndSetImage(it) }

        holder.itemView.setOnClickListener {
            val singleChatFragment = SingleChatFragment(user)
            changeMainFragments(singleChatFragment, true)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}