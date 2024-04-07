package spiral.bit.dev.sunset.other

import spiral.bit.dev.sunset.models.Member

interface UserEditListener {
    fun byOneTap(member: Member, position: Int)
    fun byOneLongTap(member: Member, position: Int)
}