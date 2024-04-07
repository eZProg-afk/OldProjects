package spiral.bit.dev.guide.models

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class MyMarker(
    var id: String,
    val position: LatLng,
    var title: String,
    var description: String
)