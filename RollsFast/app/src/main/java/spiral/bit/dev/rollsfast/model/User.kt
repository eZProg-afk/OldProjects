package spiral.bit.dev.rollsfast.model

data class User(
    val userId: String, var userName: String, val userEmail: String, val userPassword: String,
    val userPhone: String
) {
    constructor() : this(
        "", "", "",
        "", ""
    )
}