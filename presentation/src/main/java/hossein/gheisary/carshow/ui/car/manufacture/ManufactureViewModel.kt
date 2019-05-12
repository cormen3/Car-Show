package hossein.gheisary.carshow.ui.car.manufacture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureDataStore
import javax.inject.Inject

class ManufactureViewModel @Inject constructor(private val repository: ManufactureDataStore.Repository) : ViewModel() {
    val MANUFATURE_PAGE_SIZE = 15
    private val repoResult = MutableLiveData<CarDataHolder<ManufactureUiModel>>()

    var carManufactures : PagedList<ManufactureUiModel>? = null

    val items = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    fun getData() {
        repoResult.value = repository.getData(MANUFATURE_PAGE_SIZE)
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }
}