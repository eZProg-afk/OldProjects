package spiral.bit.dev.rollsfast.model

data class Product(
    val rollId: String, val rollName: String, val rollDesc: String,
    val rollPrice: String, val rollRefImage: String
) {
    constructor(): this("", "", "", "", "")
}