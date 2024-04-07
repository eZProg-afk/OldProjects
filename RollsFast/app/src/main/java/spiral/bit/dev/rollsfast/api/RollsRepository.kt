package spiral.bit.dev.rollsfast.api

import androidx.lifecycle.MutableLiveData
import spiral.bit.dev.rollsfast.model.Product

interface RollsRepository {
    fun getRolls(): MutableLiveData<List<Product?>>
}