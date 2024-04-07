package spiral.bit.dev.sunset.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_create_group.*
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.models.Group
import spiral.bit.dev.sunset.other.*

class CreateGroupFragment : Fragment() {

    private var currentTimeStamp = System.currentTimeMillis().toString()
    private var groupIcon = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_create_group, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        done_btn.setOnClickListener {
            val groupName = create_group_name_et.text.toString()
            val groupDescription = create_group_description_et.text.toString()
            if (groupName.isNotEmpty() && groupDescription.isNotEmpty())
                createGroup(groupName, groupDescription)
        }

        create_group_avatar.setOnClickListener {
            ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Добавление аватарки")
                .toolbarImageTitle("Выбрать картинку")
                .toolbarArrowColor(Color.WHITE)
                .single()
                .showCamera(true)
                .imageDirectory("Camera")
                .start()
        }

        close_btn.setOnClickListener { changeFragments(GroupsFragment(), true) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val receiveImage: Image? = ImagePicker.getFirstImageOrNull(data)
            val path = FirebaseStorage.getInstance().reference.child("AVATARS_GROUPS")
                .child(currentTimeStamp)
            putFileInStorage(Uri.parse(receiveImage?.uri.toString()), path) {
                getURL(path) {
                    putUrlGroupInDatabase(
                        it,
                        currentTimeStamp,
                        FirebaseDatabase.getInstance().reference
                    ) {
                        Toast.makeText(context, "Фото добавлено!", Toast.LENGTH_LONG).show()
                    }
                    groupIcon = it
                }
            }
        }
    }

    private fun createGroup(groupTitle: String, groupDesc: String) {
        val hashMap = HashMap<String, String>()
        hashMap["groupID"] = currentTimeStamp
        hashMap["groupTitle"] = groupTitle
        hashMap["groupDescription"] = groupDesc
        hashMap["timeStamp"] = currentTimeStamp
        hashMap["createdBy"] = USER.userId.toString()
        if (groupIcon.isNotEmpty()) hashMap["groupIcon"] = groupIcon

        FirebaseDatabase.getInstance().reference.child("users").child(USER.userId.toString())
            .child("user_groups").child(currentTimeStamp).setValue(
                Group(
                    currentTimeStamp, groupTitle,
                    groupDesc, currentTimeStamp
                )
            )

        val ref = FirebaseDatabase.getInstance().reference.child("groups")
        ref.child(currentTimeStamp).setValue(hashMap)
            .addOnSuccessListener {
                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                    var tokenId = ""
                    if (it.isComplete) tokenId = it.result.toString()
                    val hashMap2 = HashMap<String, String>()
                    //добавляем участников
                    hashMap2["uid"] = USER.userId.toString()
                    hashMap2["memberNickName"] = USER.userNickName
                    hashMap2["memberEmail"] = USER.userEmail
                    hashMap2["memberPassword"] = USER.userPassword
                    hashMap2["memberRole"] = ""
                    hashMap2["typeOfUser"] = "gm"
                    hashMap2["memberNote"] = "Creator"
                    hashMap2["tokenId"] = tokenId
                    val ref2 = FirebaseDatabase.getInstance().reference.child("groups")
                    ref2.child(currentTimeStamp).child("members").child(USER.userId.toString())
                        .setValue(hashMap2).addOnSuccessListener {
                            Toast.makeText(view?.context, "Гильдия создана!", Toast.LENGTH_SHORT)
                                .show()
                            changeFragments(GroupsFragment(), false)
                        }
                }
            }
    }
}