package spiral.bit.dev.rollsfast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_rolls.*
import spiral.bit.dev.rollsfast.model.Product
import spiral.bit.dev.rollsfast.R
import spiral.bit.dev.rollsfast.adapters.RollAdapter
import spiral.bit.dev.rollsfast.other.AppChildEventListener
import spiral.bit.dev.rollsfast.other.AppValueEventListener
import spiral.bit.dev.rollsfast.other.NODE_PRODUCTS
import spiral.bit.dev.rollsfast.other.REFERENCE_DATABASE

class RollsFragment : Fragment() {

    private lateinit var rollsAdapter: RollAdapter
    private val rolls = arrayListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =inflater.inflate(R.layout.fragment_rolls, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rollsAdapter = RollAdapter(rolls)
        rolls_recycler.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = rollsAdapter
        }

        //получаем все данные о роллах и нотифаем лист
        collectDataAndNotify()
    }

    private fun collectDataAndNotify() {
       REFERENCE_DATABASE.child(NODE_PRODUCTS)
            .child("rolls")
            .addChildEventListener(AppChildEventListener {
                REFERENCE_DATABASE.child(NODE_PRODUCTS)
                    .child("rolls")
                    .child(it.key.toString())
                    .addListenerForSingleValueEvent(AppValueEventListener { snap ->
                        val product = snap.getValue(Product::class.java)
                        if (product != null && !rolls.contains(product)) rolls.add(product)
                        rollsAdapter.submitList(rolls)
                    })
            })
    }
}