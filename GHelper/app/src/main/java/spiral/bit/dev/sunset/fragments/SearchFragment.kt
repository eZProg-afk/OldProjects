package spiral.bit.dev.sunset.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.groups_recycler
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.adapters.GroupsAdapter
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.other.AppValueEventListener

class SearchFragment : Fragment() {

    private lateinit var groupAdapter: GroupsAdapter
    private var groupsList = arrayListOf<Group>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groups_recycler.visibility = View.VISIBLE
        groupAdapter = GroupsAdapter(groupsList)
        groups_recycler.adapter = groupAdapter
        groups_recycler.layoutManager = LinearLayoutManager(view.context)
        groups_recycler.setHasFixedSize(true)

        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val firebaseSearchQuery = FirebaseDatabase.getInstance().reference.child("groups")
                    .child(s.toString())

                Handler().postDelayed({
                    firebaseSearchQuery
                        .addListenerForSingleValueEvent(AppValueEventListener {
                            val group = it.getValue(Group::class.java)
                            if (group != null && !groupsList.contains(group)) groupsList.add(group)
                            if (groupsList.isEmpty()) {
                                groups_recycler.visibility = View.GONE
                                groups_not_found_anim.visibility = View.VISIBLE
                                groups_not_found_anim.setOnClickListener { groups_not_found_anim.playAnimation() }
                                groups_not_found_tv.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(view.context, "Две секунды, уже загружаем...", Toast.LENGTH_SHORT).show()
                                groups_recycler.visibility = View.VISIBLE
                                groups_not_found_anim.visibility = View.GONE
                                groups_not_found_tv.visibility = View.GONE
                            }
                        })
                    groupAdapter.submitGroups(groupsList)
                }, 500)
            }
        })
    }
}