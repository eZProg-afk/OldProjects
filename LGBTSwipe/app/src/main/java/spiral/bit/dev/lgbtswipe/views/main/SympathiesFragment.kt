package spiral.bit.dev.lgbtswipe.views.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.lgbtswipe.R
import spiral.bit.dev.lgbtswipe.databinding.FragmentSympathiesBinding
import spiral.bit.dev.lgbtswipe.models.User
import spiral.bit.dev.lgbtswipe.other.*
import spiral.bit.dev.lgbtswipe.views.main.profile.SympathiesAdapter


class SympathiesFragment : Fragment(R.layout.fragment_sympathies) {

    private val sympBinding: FragmentSympathiesBinding by viewBinding(FragmentSympathiesBinding::bind)
    private lateinit var sympList: ArrayList<User>
    private lateinit var adapter: SympathiesAdapter
    private val idsList = arrayListOf<String>()
    val path = REFERENCE_DATABASE.child(NODE_USERS).child(CURRENT_USER_ID)
        .child(NODE_SYMPATHIES)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN_ACTIVITY.hideMenu(true)
        sympList = arrayListOf()
        adapter = SympathiesAdapter(sympList)

        getAllSympathies()

        sympBinding.sympRecycler.layoutManager = LinearLayoutManager(context)
        sympBinding.sympRecycler.adapter = adapter
        sympBinding.sympRecycler.setHasFixedSize(true)
    }

    private fun getAllSympathies() {
        path.addValueEventListener(AppValueEventListener {
            for (data in it.children) {
                val listSnapValues = data.children
                for (i in listSnapValues) {
                    idsList.add(i.value.toString())
                }
            }

            for (j in idsList) {
                REFERENCE_DATABASE.child(NODE_USERS).child(j).get().addOnSuccessListener { snap ->
                    val user = snap.getValue(User::class.java)
                    if (user != null) {
                        if (!sympList.contains(user)) {
                            sympList.add(user)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}