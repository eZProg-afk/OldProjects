package spiral.bit.dev.sunset.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.models.AvatarItem
import spiral.bit.dev.sunset.other.AvaListener

class AvatarsAdapter(var listOfAvatars: ArrayList<AvatarItem>, var avatarListener: AvaListener) :
    RecyclerView.Adapter<AvatarsAdapter.AvatarsViewHolder>() {

    private var dbUsersRef = FirebaseDatabase.getInstance().reference.child("users")
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var context: Context

    inner class AvatarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageSource: ImageView = itemView.findViewById(R.id.ava_item_source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarsViewHolder {
        context = parent.context
        return AvatarsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ava_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AvatarsViewHolder, position: Int) {
        val avatar = listOfAvatars[position]
        val myIcon = AppCompatResources.getDrawable(context, avatar.photoSource)
        holder.imageSource.setImageDrawable(myIcon)
        holder.itemView.setOnClickListener { avatarListener.onAvatarClicked(avatar, position) }
    }

    override fun getItemCount(): Int = listOfAvatars.size
}