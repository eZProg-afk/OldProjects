package spiral.bit.dev.rollsfast

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_adm.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import spiral.bit.dev.rollsfast.model.Product
import spiral.bit.dev.rollsfast.other.*
import java.util.*

class AdmActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var productCategory = ""
    private var imgRef = ""
    private var rollId = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adm)
        requestPermissions()


        product_image.setOnClickListener {
            ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Добавление аватарки")
                .toolbarImageTitle("Выбрать картинку")
                .toolbarArrowColor(Color.WHITE)
                .single()
                .showCamera(true)
                .imageDirectory("Camera")
                .start()
        }

        to_user_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val adapterCategories: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                R.layout.spin_item
            )
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        product_category_spinner.adapter = adapterCategories

        product_category_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.categories)
                productCategory = choose[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        add_product_btn.setOnClickListener {
            if (product_name_et.text.isNotEmpty() &&
                product_price_et.text.isNotEmpty() &&
                product_desc_et.text.isNotEmpty()
            ) {
                saveProduct(
                    product_name_et.text.toString(),
                    product_price_et.text.toString(),
                    product_desc_et.text.toString(),
                    productCategory,
                    imgRef
                )
            } else Toast.makeText(
                this, "Пожалуйста, заполните все поля!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveProduct(
        name: String,
        desc: String,
        price: String,
        type: String,
        productImgRef: String
    ) {
        val roll = Product(
            rollId, name, desc, price,
            productImgRef
        )
        REFERENCE_DATABASE.child(NODE_PRODUCTS).child(type)
            .child(rollId)
            .setValue(roll)
        reset()
    }

    private fun reset() {
        product_desc_et.setText("")
        product_name_et.setText("")
        product_price_et.setText("")
        product_image.setImageResource(R.drawable.picture)
        product_category_spinner.setSelection(0)
        Toast.makeText(this, "Продукт успешно добавлен!", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Toast.makeText(
                this, "Две секунды, \n загружаю картинку",
                Toast.LENGTH_SHORT
            ).show()
            val receiveImage: Image? = ImagePicker.getFirstImageOrNull(data)
            val path = REFERENCE_STORAGE.child(NODE_PRODUCTS)
                .child(rollId)
            putFileInStorage(Uri.parse(receiveImage?.uri.toString()), path) {
                getURL(path) {
                    imgRef = it
                    product_image.setImageURI(Uri.parse(receiveImage?.uri.toString()))
                }
            }
        }
    }

    private fun requestPermissions() {
        if (hasPermissions(this)) return
        EasyPermissions.requestPermissions(
            this,
            "Вам нужно принять разрешения, чтобы корректно работать с приложением!)",
            222,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else requestPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}