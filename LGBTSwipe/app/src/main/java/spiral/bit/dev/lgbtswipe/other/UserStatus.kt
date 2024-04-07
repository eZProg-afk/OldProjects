package spiral.bit.dev.lgbtswipe.other

import spiral.bit.dev.lgbtswipe.R

enum class UserStatus(val state: String) {

    ONLINE(ACTIVITY.getString(R.string.online_enum)),
    OFFLINE(ACTIVITY.getString(R.string.offline_enum)),
    TYPING(ACTIVITY.getString(R.string.typing_enum)),
    RECORDING_AUDIO(ACTIVITY.getString(R.string.recording_audio)),
    READ(ACTIVITY.getString(R.string.read_messages));

    companion object {
        fun updateState(userStatus: UserStatus) {
            if (AUTH.currentUser != null) {
                REFERENCE_DATABASE.child(
                    NODE_USERS
                ).child(CURRENT_USER_ID).child(
                    CHILD_STATE
                )
                    .setValue(userStatus.state)
                    .addOnSuccessListener { USER.state = userStatus.state }
            }
        }
    }
}