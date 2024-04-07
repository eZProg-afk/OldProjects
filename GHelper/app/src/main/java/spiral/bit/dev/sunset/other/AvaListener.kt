package spiral.bit.dev.sunset.other

import spiral.bit.dev.sunset.models.AvatarItem

interface AvaListener {
    fun onAvatarClicked(avatarItem: AvatarItem, position: Int)
}