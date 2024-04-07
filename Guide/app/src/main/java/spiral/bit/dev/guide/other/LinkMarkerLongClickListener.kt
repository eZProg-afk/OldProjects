package spiral.bit.dev.guide.other

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


abstract class LinkMarkerLongClickListener(markerList: List<Marker>) :
    OnMarkerDragListener {

    private var previousIndex = -1
    private var cachedMarker: Marker? = null
    private var cachedDefaultPostion: LatLng? = null
    private val markerList: List<Marker?>
    private val defaultPostions: MutableList<LatLng>
    abstract fun onLongClickListener(marker: Marker?)
    override fun onMarkerDragStart(marker: Marker?) {
        onLongClickListener(marker)
        setDefaultPosition(markerList.indexOf(marker))
    }

    override fun onMarkerDrag(marker: Marker?) {
        setDefaultPosition(markerList.indexOf(marker))
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        setDefaultPosition(markerList.indexOf(marker))
    }

    private fun setDefaultPosition(markerIndex: Int) {
        if (previousIndex == -1 || previousIndex != markerIndex) {
            cachedMarker = markerList[markerIndex]
            cachedDefaultPostion = defaultPostions[markerIndex]
            previousIndex = markerIndex
        }
        cachedMarker?.position = cachedDefaultPostion
    }

    init {
        this.markerList = ArrayList(markerList)
        defaultPostions = ArrayList(markerList.size)
        for (marker in markerList) {
            defaultPostions.add(marker.position)
            marker.isDraggable = true
        }
    }
}