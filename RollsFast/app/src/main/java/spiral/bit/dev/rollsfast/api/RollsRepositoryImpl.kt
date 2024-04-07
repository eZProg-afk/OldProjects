package spiral.bit.dev.fastrolls.api

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import spiral.bit.dev.rollsfast.model.Product
import spiral.bit.dev.rollsfast.api.RollsRepository

class RollsRepositoryImpl : RollsRepository {

    private val db = FirebaseDatabase.getInstance().reference.child("rolls")

    override fun getRolls(): MutableLiveData<List<Product?>> {
        val result = MutableLiveData<List<Product?>>()
        db.get().addOnSuccessListener { snap ->
            val rolls = snap.children.map { it?.getValue(Product::class.java) }
            result.postValue(rolls)
        }
        return result
    }
}