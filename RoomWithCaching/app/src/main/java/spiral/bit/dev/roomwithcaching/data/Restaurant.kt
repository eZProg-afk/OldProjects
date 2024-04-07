package spiral.bit.dev.roomwithcaching.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants_table")
data class Restaurant(
    @PrimaryKey val name: String,
    val type: String,
    val logo: String,
    val address: String
)
