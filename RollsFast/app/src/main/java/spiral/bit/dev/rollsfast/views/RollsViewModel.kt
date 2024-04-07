package spiral.bit.dev.rollsfast.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import spiral.bit.dev.fastrolls.api.RollsRepositoryImpl
import spiral.bit.dev.rollsfast.model.Product
import spiral.bit.dev.rollsfast.api.RollsRepository

class RollsViewModel : ViewModel() {

    private val repository: RollsRepository = RollsRepositoryImpl()
    private var rolls = MutableLiveData<List<Product?>>()

    fun getRolls(): LiveData<List<Product?>> {
        if(rolls.value.isNullOrEmpty()) {
            rolls = repository.getRolls()
        }
        return rolls
    }
}