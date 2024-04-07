package spiral.bit.dev.sunset.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_groups.*
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.adapters.GroupsAdapter
import spiral.bit.dev.sunset.adapters.GroupsInFragmentAdapter
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.other.AppChildEventListener
import spiral.bit.dev.sunset.other.AppValueEventListener
import spiral.bit.dev.sunset.other.USER
import spiral.bit.dev.sunset.other.changeFragments

@Suppress("RedundantIf")
class GroupsFragment : Fragment() {

    private var dbUsersRef = FirebaseDatabase.getInstance().reference.child("users")
    private var groupsList = arrayListOf<Group>()
    private lateinit var groupsAdapter: GroupsInFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_groups, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_group.setOnClickListener { changeFragments(CreateGroupFragment(), true) }
        groupsAdapter = GroupsInFragmentAdapter(groupsList)
        if (groupsList.isEmpty()) getGroups()

        groups_recycler.visibility = View.VISIBLE
        groups_recycler.setHasFixedSize(true)
        groups_recycler.layoutManager = LinearLayoutManager(view.context)
        groups_recycler.adapter = groupsAdapter
        groups_anim.visibility = View.GONE
        groups_empty_tv.visibility = View.GONE
    }

    private fun getGroups() {
        dbUsersRef.child(USER.userId.toString()).child("user_groups")
            .addChildEventListener(AppChildEventListener {
                dbUsersRef.child(USER.userId.toString()).child("user_groups")
                    .child(it.key.toString())
                    .addListenerForSingleValueEvent(AppValueEventListener { snap ->
                        val group = snap.getValue(Group::class.java)
                        if (group != null && !groupsList.contains(group))
                            groupsList.add(group)
                        groupsAdapter.submitGroups(groupsList)
                        groupsAdapter.notifyDataSetChanged()
                    })
            })
        if (groupsList.isEmpty()) {
            groups_recycler.visibility = View.GONE
            groups_anim.visibility = View.VISIBLE
            groups_anim.setOnClickListener { groups_anim.playAnimation() }
            groups_empty_tv.visibility = View.VISIBLE
        }
    }
}