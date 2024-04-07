package spiral.bit.dev.sunset.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import spiral.bit.dev.sunset.MainActivity
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.models.Member
import spiral.bit.dev.sunset.other.*

class MembersAdapter(var listOfMembers: ArrayList<Member>, var userEditListener: UserEditListener) :
    RecyclerView.Adapter<MembersAdapter.GroupsViewHolder>() {

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var context: Context

    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickName: TextView = itemView.findViewById(R.id.item_user_nick_name)
        val userRole: TextView = itemView.findViewById(R.id.item_user_role)
        val userNote: TextView = itemView.findViewById(R.id.item_user_note)
        val userAvatar: RoundedImageView = itemView.findViewById(R.id.item_user_avatar)
        val backConstr: ConstraintLayout = itemView.findViewById(R.id.back_constr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        context = parent.context
        return GroupsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        val member = listOfMembers[position]
        holder.userNickName.text = member.memberNickName
        holder.userRole.text = member.memberRole
        holder.userNote.text = member.memberNote
        holder.itemView.setOnClickListener { userEditListener.byOneTap(member, position) }
        holder.itemView.setOnLongClickListener {
            userEditListener.byOneLongTap(member, position)
            holder.backConstr.setBackgroundResource(R.drawable.background_check_item)
            return@setOnLongClickListener true
        }
        val path = storage.reference.child("AVATARS_USERS").child(member.uid.toString())
        getURL(path) { holder.userAvatar.downloadAndSetImage(it) }
    }

    override fun getItemCount(): Int = listOfMembers.size

    fun submitGroups(listGroups: ArrayList<Member>) {
        listOfMembers = listGroups
        notifyDataSetChanged()
    }
}