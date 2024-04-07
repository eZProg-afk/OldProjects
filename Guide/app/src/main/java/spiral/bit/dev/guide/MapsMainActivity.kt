package spiral.bit.dev.guide

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.directions.route.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps_main.*
import spiral.bit.dev.guide.models.MyMarker
import spiral.bit.dev.guide.other.LinkMarkerLongClickListener
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SENSELESS_COMPARISON")
class MapsMainActivity : AppCompatActivity(), OnMapReadyCallback, RoutingListener {

    private lateinit var mMap: GoogleMap
    private var marksList = arrayListOf<Marker>()
    private var start: LatLng? = null
    private var end: LatLng? = null
    private var polylines = ArrayList<Polyline>()
    var myLocation: Location? = null
    var locationPermission = false
    private lateinit var manager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_main)
        manager = getSystemService(LOCATION_SERVICE) as LocationManager
        requestPermision()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Кажется ваш GPS выключен, хотите включить его?")
            .setCancelable(false)
            .setPositiveButton(
                "Да"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "Нет"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun requestPermision() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                121
            )
        } else {
            locationPermission = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            121 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermission = true
                    getMyLocation()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) buildAlertMessageNoGps();
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationChangeListener { location ->
            myLocation = location
            val ltlng = LatLng(location.latitude, location.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                ltlng, 16f
            )
            mMap.animateCamera(cameraUpdate)
        }

        mMap.setOnMarkerDragListener(object : LinkMarkerLongClickListener(marksList) {
            override fun onLongClickListener(marker: Marker?) {
                if (marker != null) {
                    end = marker.position
                    start = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                    findRoutes(start, end)
                }
            }
        })

        mMap.setOnMarkerClickListener {
            showMarkerDialog(it, this)
            return@setOnMarkerClickListener true
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fillSightsList()
        if (locationPermission) getMyLocation()
    }

    private fun showMarkerDialog(marker: Marker, context: Context) {
        val builder = AlertDialog.Builder(context)
        val view: View = LayoutInflater.from(context)
            .inflate(
                R.layout.dialog_show_description,
                findViewById(R.id.layout_delete_note_container)
            )
        builder.setView(view)
        val dialogMarker = builder.create()
        if (dialogMarker.window != null) dialogMarker.window?.setBackgroundDrawable(ColorDrawable(0))
        val tvTitle = view.findViewById<TextView>(R.id.marker_title)
        val tvDesc = view.findViewById<TextView>(R.id.text_description_marker)
        tvTitle.text = marker.title
        tvDesc.text = marker.snippet
        view.findViewById<View>(R.id.text_cancel).setOnClickListener {
            dialogMarker.dismiss()
        }
        dialogMarker.show()
    }

    private fun fillSightsList() {
        val marker0 = MyMarker(
            "0", LatLng(54.77875278713955, 32.05277147081097),
            "Смоленская крепость", "" +
                    "Смоленская крепостная стена — городская стена Смоленска " +
                    "протяжённостью 6,5 км, построенная в 1595—1602 гг. под руководством " +
                    "зодчего Фёдора Коня. Имела огромное оборонное значение в Российском государстве." +
                    " Большая часть крепости была уничтожена в ночь" +
                    " с 4 на 5 ноября (по новому стилю — 17 ноября) 1812 года отступающими войсками " +
                    "императора Наполеона I. Сохранилось меньше половины стен и башен."
        )
        val marker1 = MyMarker(
            "1", LatLng(54.78865607704904, 32.05441348615112),
            "Успенский собор", "" +
                    "Собо́р Успе́ния Пресвято́й Богоро́дицы — кафедральный собор Смоленской митрополии" +
                    " Русской православной церкви. Находится в центральной части города " +
                    "Смоленска на Соборной горе. Возведён на рубеже XVII и XVIII веков в память о " +
                    "героической обороне Смоленска 1609—1611 годов на месте одноимённого собора XII века. " +
                    "Главный престол освящён в честь Успения Пресвятой Богородицы. Внутри сохранились иконостас " +
                    "и убранство в стиле киевского барокко."
        )
        val marker2 = MyMarker(
            "2", LatLng(54.78218318801827, 32.04772258430613),
            "Скульптура Оленя", "Скульптура Оленя — творение немецкого анималиста Рихарда Фризе," +
                    " первоначально установленное в Роминстенской пуще, перемещённое после Великой Отечественной войны " +
                    "в смоленский сад Блонье."
        )
        val marker3 = MyMarker(
            "3",
            LatLng(54.77949463450711, 32.04749659204779),
            "Благодарная Россия - Героям 1812 года",
            "Памятник «Благодарная Россия — Героям 1812 года» (также известен как «памятник с орлами»)" +
                    " — один из самых известных памятников Смоленска, был воздвигнут в честь 100-летия" +
                    " Отечественной войны 1812 года."
        )
        val marker4 = MyMarker(
            "4",
            LatLng(54.78056796712918, 32.04615459166579),
            "Художественная галерея",
            "Смоленская художественная галерея — художественный музей в составе Смоленского " +
                    "государственного музея-заповедника. Находится в историческом центре города, " +
                    "в здании бывшего Александровского реального училища. Располагает значимым собранием " +
                    "отечественной и западноевропейской живописи, икон, предметов декоративно-прикладного искусства."
        )
        val marker5 = MyMarker(
            "5",
            LatLng(54.782310, 32.049996),
            "Исторический музей",
            "Исторический музей является старейшим в системе Смоленского музея-заповедника: в 1888 году " +
                    "смоленским краеведом С.П.Писаревым в здании городской Думы был открыт историко-археологический" +
                    " музей. С тех пор коллекции значительно расширились, музей сменил название и адрес," +
                    " но по-прежнему знакомит посетителей с богатой историей Смоленщины."
        )
        val marker6 = MyMarker(
            "6",
            LatLng(54.783228, 32.046741),
            "Драматический театр имени А. С.Грибоедова",
            "Смоле́нский госуда́рственный академи́ческий драмати́ческий теа́тр имени А. С. Грибоедова" +
                    " — театр в городе Смоленске, один из старейших действующих драматических театров России." +
                    " Основан в 1780 году."
        )
        val marker7 = MyMarker(
            "7",
            LatLng(54.780069, 32.043760),
            "Музей Смоленск - щит России",
            "Музей «Смоленск — щит России» — один из смоленских музеев, посвящённый боевой истории Смоленска" +
                    " и его роли в истории России. Располагается в Громовой башне Смоленской " +
                    "крепостной стены."
        )
        val marker8 = MyMarker(
            "8",
            LatLng(54.780348, 32.059006),
            "Музей Смоленский лен",
            "Музей «Смоленский лён» — музей в Смоленске, входит в состав Смоленского государственного" +
                    " музея-заповедника. Создан в 1980 году. Самый старый в России музей льна."
        )
        val marker9 = MyMarker(
            "9",
            LatLng(54.781299, 32.049064),
            "Музей скульптуры С.Т. Коненкова",
            "Мемориальный музей-мастерская Серге́я Конёнкова — мемориальный музей скульптора " +
                    "Сергея Конёнкова, основанный в 1974 году. Является филиалом Научно-исследовательского музея" +
                    " Российской академии художеств."
        )
        val marker10 = MyMarker(
            "10",
            LatLng(54.781954, 32.050337),
            "Городская кузница",
            "Кузница — самое старое светское здание города Смоленска. Расположена по адресу: ул. Ленина," +
                    " д.8а. Дом построен на рубеже XVII — XVIII веков (точная дата неизвестна) и является самым" +
                    " древним гражданским зданием на Смоленщине. Вначале, во времена, когда Смоленск находился в " +
                    "составе Речи Посполитой, домик служил городским архивом («польская архива»). \n" +
                    "В 1785 году «польскую архиву» приспособили под кузницу Инженерного дома." +
                    " Впоследствии инженерный дом был разрушен, а кузница сохранилась. " +
                    "Сейчас кузница запрятана в одном из городских дворов и немного углублена в землю." +
                    " С 1982 года в этом доме разместился Музей кузнечного ремесла, в котором представлены подлинные " +
                    "кузнечные инструменты, наковальня, меха и изделия кузнечного ремесла XVII — XIX веков. "
        )
        val marker11 = MyMarker(
            "11",
            LatLng(54.77930531838808, 32.04757243032552),
            "Музей Смоленщина в годы ВОВ",
            "Музей “Смоленщина в годы Великой Отечественной войны” – один из самых посещаемых музеев города;" +
                    " он интересен как молодежи, так и людям старшего поколения.\n" +
                    "Музей открылся 24 сентября 1973 г. к 30-летию освобождения Смоленщины от" +
                    " немецко-фашистских захватчиков. Расположился он в здании городского училища “Памяти 1812 года”," +
                    " построенном в 1912 г. и находящемся в сквере Памяти героев."
        )
        val marker12 = MyMarker(
            "12",
            LatLng(54.77559425592619, 31.78902647081082),
            "Мемориальный комплекс Катынь",
            "Мемориа́льный ко́мплекс «Ка́тынь» — международный мемориал жертвам политических репрессий. " +
                    "Расположен в Катынском лесу. На территории мемориала расположено военное кладбище," +
                    " на котором похоронены 4415 польских военнопленных офицеров — заключённые Козельского лагеря," +
                    " расстрелянные весной 1940 года сотрудниками НКВД СССР. Также на территории Катынского лесного" +
                    " массива захоронено около 6,5 тысяч жертв сталинских репрессий, казнённых в 1930-х годах," +
                    " и 500 советских военнопленных, расстрелянных немецкими оккупационными войсками в 1943 год."
        )
        val marker13 = MyMarker(
            "13",
            LatLng(54.79657797488126, 32.03772708246203),
            "Петропавловская и Варваринская церкви",
            "Петропа́вловская це́рковь на Городя́нке — один из трёх сохранившихся в Смоленске памятников" +
                    " домонгольского зодчества. Храм был построен в 1146 году при смоленском князе " +
                    "Ростиславе Мстиславиче. Освятил его первый смоленский епископ — Симеон." +
                    "Первое документальное упоминание о храме встречается на гравюре Вильгельма Гондиуса."
        )
        val marker14 = MyMarker(
            "14",
            LatLng(54.790584, 32.019008),
            "Храм Михаила Архангела",
            "Церковь Михаила Архангела на Пристани (Свирская церковь) — каменный столпообразный " +
                    "православный храм в честь Архангела Михаила, построенный в конце XII века на загородном дворе " +
                    "смоленских князей. Самый яркий из сохранившихся памятников смоленской архитектуры домонгольского " +
                    "времени. Стоит на берегу Днепра в 1,5 км к западу от центра современного Смоленска (ул. Парковая, д. 2А)." +
                    " Современный облик получил после реставрации 1976—82 гг."
        )
        val marker15 = MyMarker(
            "15",
            LatLng(54.773397, 32.053623),
            "Католический костёл",
            "Храм Непорочного Зачатия Пресвятой Девы Марии — недействующий католический храм" +
                    " в Смоленске, построенный в неоготическом стиле. Находится на улице Урицкого." +
                    " Храм возводился на Костельной улице за Молоховскими воротами в конце XIX века " +
                    "вследствие того, что предыдущий храм во имя Рождества Пресвятой Богородицы стал " +
                    "тесен из-за увеличивающегося количества прихожан.\n" +
                    "Автором проекта, выполненного в неоготическом стиле, был М.Ф. Мейшнер."
        )
        val marker16 = MyMarker(
            "16",
            LatLng(54.797954, 32.034519),
            "Здание железнодорожного вокзала",
            "Станция открыта в 1868 году в составе линии Орловско-Витебской железной дороги, " +
                    "а в 1870 году открыт проложенный параллельно ей первый участок Москва — Смоленск " +
                    "Московско-Брестской железной дороги. До революций 1917 года было два пассажирских вокзала" +
                    " — Орловско-Витебской (Риго-Орловской) и Московско-Брестской (Александровской) железных дорог," +
                    " оба вокзальных здания стояли параллельно друг другу между железнодорожными путями, и были" +
                    " соединены между собой аркой с часами."
        )
        val marker17 = MyMarker(
            "17",
            LatLng(54.790725, 32.046414),
            " Набережная реки Днепр",
            "Набережная Днепра в Смоленске — располагается в центральной части города (в Ленинском районе)." +
                    "У набережной три уровня: нижний - у Днепра, верхний - у улицы Большая Краснофлотская и средний.\n" +
                    "Набережная Днепра прекрасное место для прогулки. Отсюда открываются замечательные виды на реку " +
                    "Днепр и Заднепровский район города. Здесь много скамеек, качелей, есть детская игровая площадка." +
                    " Памятник князю Владимиру расположен на Набережной Днепра, у улицы Большая Краснофлотская" +
                    " (на пересечении со Студенческой улицей)."
        )
        val marker18 = MyMarker(
            "18",
            LatLng(54.781467, 32.046329),
            "Сад Блонье",
            "Сад Блонье — парк в центре Смоленска, одна из достопримечательностей города." +
                    " В черте города местность Блонье оказалась в XVI веке. Сад Блонье официально был заложен" +
                    " в 1830 году. В это время ровную площадку у стен Смоленской крепости, использовавшуюся" +
                    " как плацдарм для муштры солдат, указом императора Николая I стали переоборудовать под сад. " +
                    "Руководил строительством тогдашний губернатор Смоленщины Николай Иванович Хмельницкий."
        )
        val marker19 = MyMarker(
            "19",
            LatLng(54.783035, 32.038918),
            "Лопатинский сад",
            "Лопатинский сад — парк в центре Смоленска, одна из городских достопримечательностей. " +
                    "В 1874 году на месте бывшей Королевской крепости по приказу губернатора Александра " +
                    "Григорьевича Лопатина был создан сад, впоследствии названный его именем. Первоначально сад" +
                    " ограничивался валами Королевской крепости и планировался в ландшафтном стиле, чему во многом" +
                    " содействовали живописность прилегающей местности, а также изобилие памятников смоленской истории."
        )

        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker0.position).title(marker0.title)
                    .snippet(marker0.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker1.position).title(marker1.title)
                    .snippet(marker1.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker2.position).title(marker2.title)
                    .snippet(marker2.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker3.position).title(marker3.title)
                    .snippet(marker3.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker4.position).title(marker4.title)
                    .snippet(marker4.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker5.position).title(marker5.title)
                    .snippet(marker5.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker6.position).title(marker6.title)
                    .snippet(marker6.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker7.position).title(marker7.title)
                    .snippet(marker7.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker8.position).title(marker8.title)
                    .snippet(marker8.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker9.position).title(marker9.title)
                    .snippet(marker9.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker10.position).title(marker10.title)
                    .snippet(marker10.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker11.position).title(marker11.title)
                    .snippet(marker11.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker12.position).title(marker12.title)
                    .snippet(marker12.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker13.position).title(marker13.title)
                    .snippet(marker13.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker14.position).title(marker14.title)
                    .snippet(marker14.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker15.position).title(marker15.title)
                    .snippet(marker15.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker16.position).title(marker16.title)
                    .snippet(marker16.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker17.position).title(marker17.title)
                    .snippet(marker17.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker18.position).title(marker18.title)
                    .snippet(marker18.description)
            )
        )
        marksList.add(
            mMap.addMarker(
                MarkerOptions().position(marker19.position).title(marker19.title)
                    .snippet(marker19.description)
            )
        )
    }

    private fun findRoutes(first: LatLng?, second: LatLng?) {
        when {
            start == null -> {
                Toast.makeText(this, "Выберите отправную точку!", Toast.LENGTH_SHORT).show()
                return
            }
            end == null -> {
                Toast.makeText(this, "Выберите конечную точку!", Toast.LENGTH_LONG).show()
                return
            }
            else -> {
                val routing = Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(first, second)
                    .key("AIzaSyChN0l29xh89ouq_GToAj_5hw7LmTF-Gzc")
                    .build()
                routing.execute()
            }
        }
    }

    override fun onRoutingFailure(p0: RouteException?) {
    }

    override fun onRoutingStart() {
    }

    override fun onRoutingSuccess(routes: ArrayList<Route>?, shortestRouteIndex: Int) {
        val center = CameraUpdateFactory.newLatLngZoom(start, 16f)
        mMap.moveCamera(center)
        if (polylines.size > 0) for (poly in polylines) poly.remove()
        polylines = ArrayList()
        for (i in 0 until routes!!.size) {
            if (i == shortestRouteIndex) {
                val polyOptions = PolylineOptions()
                polyOptions.color(Color.BLUE)
                polyOptions.width(7f)
                polyOptions.addAll(routes[shortestRouteIndex].points)
                val polyline = mMap.addPolyline(polyOptions)
                start = polyline.points[0]
                val k = polyline.points.size
                end = polyline.points[k - 1]
                polylines.add(polyline)
            }
        }
        start = null
        end = null
    }

    override fun onRoutingCancelled() {
    }
}