package spiral.bit.dev.sunset.models

class Member(
    var uid: String?, var memberNickName: String, var memberEmail: String,
    var memberPassword: String, var memberRole: String, var memberNote: String,
    var typeOfUser: String, var tokenId: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}