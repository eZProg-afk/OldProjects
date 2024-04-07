package spiral.bit.dev.sunset.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_inside_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.adapters.MembersAdapter
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.models.Member
import spiral.bit.dev.sunset.other.*

class InsideGroupFragment(var group: Group) : Fragment(), UserEditListener {

    private lateinit var storage: FirebaseStorage
    private lateinit var membersAdapter: MembersAdapter
    private var listOfMembers = arrayListOf<Member>()
    private var tempMembers = arrayListOf<Member>()
    private lateinit var meMember: Member

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_inside_group, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = FirebaseStorage.getInstance()
        val ref = FirebaseDatabase.getInstance().reference.child("groups")
        ref.orderByChild("groupID").equalTo(group.groupID)
            .addValueEventListener(AppValueEventListener { snap ->
                for (ds: DataSnapshot in snap.children) {
                    val groupTitle = ds.child("groupTitle").value
                    val groupDescription = ds.child("groupDescription").value
                    val createdBy = ds.child("createdBy").value
                    if (inside_group_name_et != null) inside_group_name_et.text =
                        groupTitle.toString()
                    if (inside_group_description_et != null) inside_group_description_et.text =
                        groupDescription.toString()
                    if (inside_group_id != null) inside_group_id.text = group.groupID
                    val path =
                        storage.reference.child("AVATARS_GROUPS").child(group.groupID)
                    getURL(path) { inside_group_avatar.downloadAndSetImage(it) }
                }
            })

        membersAdapter = MembersAdapter(listOfMembers, this)
        inside_members_recycler.layoutManager = LinearLayoutManager(view.context)
        inside_members_recycler.setHasFixedSize(true)
        inside_members_recycler.adapter = membersAdapter

        leave_group_btn.setOnClickListener { leaveGroup() }
        collectDataAndNotify()
    }

    private fun collectDataAndNotify() {
        if (listOfMembers.isNotEmpty()) listOfMembers.clear()
        FirebaseDatabase.getInstance().reference.child("groups")
            .child(group.groupID).child("members")
            .addChildEventListener(AppChildEventListener {
                FirebaseDatabase.getInstance().reference.child("groups")
                    .child(group.groupID).child("members")
                    .child(it.key.toString())
                    .addListenerForSingleValueEvent(AppValueEventListener { snap ->
                        val member = snap.getValue(Member::class.java)
                        if (member != null && !listOfMembers.contains(member)) {
                            if (member.uid.toString() == USER.userId.toString()) meMember = member
                            listOfMembers.add(member)
                        }
                        membersAdapter.submitGroups(listOfMembers)
                    })
            })
    }

    private fun leaveGroup() {
        FirebaseDatabase.getInstance().reference.child("groups")
            .child(group.groupID).child("members")
            .child(meMember.uid.toString())
            .setValue(null)

        FirebaseDatabase.getInstance().reference.child("users")
            .child(USER.userId.toString())
            .child("user_groups")
            .child(group.groupID)
            .setValue(null)
        collectDataAndNotify()
        changeFragments(GroupsFragment(), false)
    }

    private fun showSendPushDialog(viewMain: View) {
        val builder = AlertDialog.Builder(viewMain.context)
        val view: View = LayoutInflater.from(context)
            .inflate(
                R.layout.layout_send_push_dialog,
                viewMain.findViewById(R.id.layout_send_push_dialog_container)
            )
        builder.setView(view)
        val dialogDeleteNote = builder.create()
        if (dialogDeleteNote.window != null) dialogDeleteNote.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        val etNote = view.findViewById<EditText>(R.id.edit_push_title)
        val etDesc = view.findViewById<EditText>(R.id.edit_push_desc)
        etNote.isEnabled = true
        view.findViewById<View>(R.id.text_send_note).setOnClickListener {
            dialogDeleteNote.dismiss()
        }
        view.findViewById<TextView>(R.id.text_delete_note).setOnClickListener {
            if (etNote.text.isNotEmpty() && etDesc.text.isNotEmpty()) {
                tempMembers.forEach {
                    sendPushes(
                        Push(
                            (PushData(etNote.text.toString(), etDesc.text.toString())),
                            it.tokenId
                        )
                    )
                }
                apply_pushes.visibility = View.GONE
                cancel_pushes.visibility = View.GONE
                tempMembers.clear()
            } else Toast.makeText(
                view.context,
                "Пожалуйста, заполните все поля!",
                Toast.LENGTH_SHORT
            ).show()
            dialogDeleteNote.dismiss()
        }
        dialogDeleteNote.show()
    }

    private fun showEditMemberDialog(viewMain: View, member: Member) {
        val builder = AlertDialog.Builder(viewMain.context)
        val view: View = LayoutInflater.from(context)
            .inflate(
                R.layout.layout_edit_member_dialog,
                viewMain.findViewById(R.id.layout_select_ava_container)
            )
        builder.setView(view)
        val dialogDeleteNote = builder.create()
        if (dialogDeleteNote.window != null) dialogDeleteNote.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        view.findViewById<TextView>(R.id.edit_user_nick_name).text = member.memberNickName
        val etNote = view.findViewById<TextView>(R.id.edit_user_note)
        etNote.text = member.memberNote
        val editRoleSpinner = view.findViewById<Spinner>(R.id.edit_role_spinner)
        val editTypeSpinner = view.findViewById<Spinner>(R.id.edit_type_of_spinner)
        val editRoleLabel = view.findViewById<TextView>(R.id.edit_user_role_label)
        val editTypeOfUserLabel = view.findViewById<TextView>(R.id.edit_user_type_label)
        if (meMember.typeOfUser == "gm") {
            etNote.isEnabled = true
            editRoleLabel.visibility = View.VISIBLE
            editTypeOfUserLabel.visibility = View.VISIBLE
            editTypeSpinner.visibility = View.VISIBLE

            view.findViewById<TextView>(R.id.text_delete_note).setOnClickListener {
                if (!etNote.text.equals("")) {
                    FirebaseDatabase.getInstance().reference.child("groups")
                        .child(group.groupID).child("members").child(member.uid.toString())
                        .child("memberNote").setValue(etNote.text.toString())
                    collectDataAndNotify()
                    dialogDeleteNote.dismiss()
                }
                dialogDeleteNote.dismiss()
            }

            val adapterRole: ArrayAdapter<*> =
                ArrayAdapter.createFromResource(
                    view.context,
                    R.array.roles,
                    R.layout.spinner_item
                )
            editRoleSpinner.adapter = adapterRole

            editRoleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?, selectedItemPosition: Int, selectedId: Long
                ) {
                    val choose = resources.getStringArray(R.array.roles)
                    FirebaseDatabase.getInstance().reference.child("groups")
                        .child(group.groupID).child("members").child(member.uid.toString())
                        .child("memberRole")
                        .setValue(choose[selectedItemPosition])
                    collectDataAndNotify()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            val adapterType: ArrayAdapter<*> =
                ArrayAdapter.createFromResource(
                    view.context,
                    R.array.types_of_users,
                    R.layout.spinner_item
                )

            if (meMember.typeOfUser == "gm" && !member.uid.equals(meMember.uid)) {
                editTypeSpinner.adapter = adapterType

                editTypeSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            itemSelected: View?, selectedItemPosition: Int, selectedId: Long
                        ) {
                            val choose = resources.getStringArray(R.array.types_of_users)
                            FirebaseDatabase.getInstance().reference.child("groups")
                                .child(group.groupID).child("members").child(member.uid.toString())
                                .child("typeOfUser")
                                .setValue(choose[selectedItemPosition])
                            collectDataAndNotify()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            } else {
                editTypeOfUserLabel.visibility = View.GONE
                editTypeSpinner.visibility = View.GONE
            }
        } else if (meMember.typeOfUser == "officer") {
            etNote.isEnabled = true
            editRoleLabel.visibility = View.VISIBLE
            editTypeOfUserLabel.visibility = View.GONE
            editTypeSpinner.visibility = View.GONE

            view.findViewById<TextView>(R.id.text_delete_note).setOnClickListener {
                if (!etNote.text.equals("")) {
                    FirebaseDatabase.getInstance().reference.child("groups")
                        .child(group.groupID).child("members").child(member.uid.toString())
                        .child("memberNote").setValue(etNote.text.toString())
                    dialogDeleteNote.dismiss()
                    collectDataAndNotify()
                }
                dialogDeleteNote.dismiss()
            }

            val adapterRole: ArrayAdapter<*> =
                ArrayAdapter.createFromResource(
                    view.context,
                    R.array.roles,
                    R.layout.spinner_item
                )
            editRoleSpinner.adapter = adapterRole

            editRoleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?, selectedItemPosition: Int, selectedId: Long
                ) {
                    val choose = resources.getStringArray(R.array.roles)
                    FirebaseDatabase.getInstance().reference.child("groups")
                        .child(group.groupID).child("members").child(member.uid.toString())
                        .child("memberRole")
                        .setValue(choose[selectedItemPosition])
                    collectDataAndNotify()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        } else {
            editRoleSpinner.visibility = View.GONE
            editTypeSpinner.visibility = View.GONE
            editRoleLabel.visibility = View.GONE
            editTypeOfUserLabel.visibility = View.GONE
            etNote.isEnabled = false
            view.findViewById<TextView>(R.id.text_delete_note).setOnClickListener {
                dialogDeleteNote.dismiss()
            }
        }
        dialogDeleteNote.show()
    }

    override fun byOneTap(member: Member, position: Int) {
        showEditMemberDialog(requireView(), member)
    }

    override fun byOneLongTap(member: Member, position: Int) {
        if (meMember.typeOfUser != "user") {
            tempMembers.add(member)
            apply_pushes.visibility = View.VISIBLE
            cancel_pushes.visibility = View.VISIBLE
            apply_pushes.setOnClickListener { view?.let { it1 -> showSendPushDialog(it1) } }
            cancel_pushes.setOnClickListener {
                tempMembers.clear()
                apply_pushes.visibility = View.GONE
                cancel_pushes.visibility = View.GONE
            }
        }
    }

    private fun sendPushes(push: Push) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetroInstance.api.postNotification(push)
                if (!response.isSuccessful)
                    Toast.makeText(
                        view?.context,
                        "Ошибка:( \n Response: ${response.errorBody().toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}