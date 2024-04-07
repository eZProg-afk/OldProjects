package spiral.bit.dev.sunset.models

import java.io.Serializable

class User(
    var userId: String?, var userNickName: String, var userEmail: String,
    var userPassword: String,
): Serializable {
    constructor(): this(
        "",
        "",
        "",
        ""
    )
}