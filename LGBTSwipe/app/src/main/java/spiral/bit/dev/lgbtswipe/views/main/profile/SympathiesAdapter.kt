package spiral.bit.dev.lgbtswipe.views.main.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.ProfileFragment

class SympathiesAdapter(var listOfSymp: ArrayList<User>) : RecyclerView.Adapter<SympathiesAdapter.SympViewHolder>() {

    inner class SympViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.main_list_item_name)
        val lastMsgTv: TextView = itemView.findViewById(R.id.main_list_last_message)
        val userAva: RoundedImageView = itemView.findViewById(R.id.main_list_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SympViewHolder {
        return SympViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.main_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SympViewHolder, position: Int) {
        val user = listOfSymp[position]

        val path = REFERENCE_STORAGE.child(AVATARS_USERS)
            .child(user.id)

        holder.nameTv.text = user.name
        holder.lastMsgTv.text = user.gender
        getURL(path) { holder.userAva.downloadAndSetImage(it) }
        holder.itemView.setOnClickListener {
            changeMainFragments(ProfileFragment(user), true)
        }
    }

    override fun getItemCount(): Int = listOfSymp.size

    fun updateListItems(user: User) = listOfSymp.add(user)
}