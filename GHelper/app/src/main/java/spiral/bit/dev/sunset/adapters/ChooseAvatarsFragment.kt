package spiral.bit.dev.sunset.adapters

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AnyRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_choose_avatars.*
import spiral.bit.dev.sunset.R
import spiral.bit.dev.sunset.fragments.ProfileFragment
import spiral.bit.dev.sunset.models.AvatarItem
import spiral.bit.dev.sunset.other.*


class ChooseAvatarsFragment : Fragment(), AvaListener {

    private lateinit var adapter: AvatarsAdapter
    private var listOfAvatars = arrayListOf<AvatarItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_choose_avatars, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listOfAvatars.add(AvatarItem(0, R.drawable.ten))
        listOfAvatars.add(AvatarItem(1, R.drawable.eleven))
        listOfAvatars.add(AvatarItem(2, R.drawable.twelve))
        listOfAvatars.add(AvatarItem(3, R.drawable.thirteen))
        listOfAvatars.add(AvatarItem(4, R.drawable.fourteen))
        listOfAvatars.add(AvatarItem(5, R.drawable.fifteen))
        listOfAvatars.add(AvatarItem(6, R.drawable.sixteen))
        listOfAvatars.add(AvatarItem(7, R.drawable.seventeen))
        listOfAvatars.add(AvatarItem(8, R.drawable.eighteen))
        adapter = AvatarsAdapter(listOfAvatars, this)
        ava_recycler.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        ava_recycler.setHasFixedSize(true)
        ava_recycler.adapter = adapter
    }

    override fun onAvatarClicked(avatarItem: AvatarItem, position: Int) {
        val photoUri = view?.context?.let { getUriToDrawable(it, avatarItem.photoSource) }
        val path = FirebaseStorage.getInstance().reference.child("AVATARS_USERS")
            .child(USER.userId.toString())
        if (photoUri != null) {
            putFileInStorage(photoUri, path) {
                getURL(path) {
                    putUrlInDatabase(it, USER.userId.toString(), FirebaseDatabase.getInstance().reference) {
                        Toast.makeText(context, "Фото добавлено!", Toast.LENGTH_LONG).show()
                        changeFragments(ProfileFragment(), true)
                    }
                }
            }
        }
    }

    private fun getUriToDrawable(
        context: Context,
        @AnyRes drawableId: Int
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + context.resources.getResourcePackageName(drawableId)
                    + '/' + context.resources.getResourceTypeName(drawableId)
                    + '/' + context.resources.getResourceEntryName(drawableId)
        )
    }
}