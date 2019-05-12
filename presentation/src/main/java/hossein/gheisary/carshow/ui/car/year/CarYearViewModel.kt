package hossein.gheisary.carshow.ui.car.year

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hossein.gheisary.carshow.ui.car.year.model.CarYearDataStore
import hossein.gheisary.carshow.utility.extensions.toLiveData
import hossein.gheisary.data.remote.model.CarYearResponse
import hossein.gheisary.data.remote.model.Outcome
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CarYearViewModel @Inject constructor(private val repository: CarYearDataStore.Repository,
                                           private val compositeDisposable: CompositeDisposable) : ViewModel() {

    var manufactureName:String? =""
    var carType:String? =""
    var carId:String? =""
    var yearsData:CarYearResponse? = null

    val activeWorksDetailOutcome: LiveData<Outcome<CarYearResponse>> by lazy {
        repository.carYearOutcome.toLiveData(compositeDisposable)
    }

    fun getCarYearData(id:String?, type:String?) {
        repository.getCarYearData(id, type)
    }

}