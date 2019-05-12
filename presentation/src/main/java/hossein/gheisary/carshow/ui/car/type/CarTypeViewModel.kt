package hossein.gheisary.carshow.ui.car.type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.type.model.CarTypeDataStore
import javax.inject.Inject

class CarTypeViewModel @Inject constructor(private val repository: CarTypeDataStore.Repository) : ViewModel() {
    private val repoResult = MutableLiveData<CarDataHolder<CarTypeUiModel>>()

    val CAR_TYPE_PAGE_SIZE = 15

    var manufactureId:String? =""
    var manufactureName:String? =""

    var carModels : PagedList<CarTypeUiModel>? = null

    val items = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    fun getData(id:String?) {
        repoResult.value = repository.getData(id, CAR_TYPE_PAGE_SIZE)
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }
}