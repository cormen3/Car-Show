package hossein.gheisary.carshow.ui.car.type.model

import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.carshow.ui.car.type.CarTypeUiModel
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class CarTypeDataStore {
    interface Repository {
        val refreshStatePublishSubject: PublishSubject<NetworkState>
        fun getData(id:String?, pageSize: Int): CarDataHolder<CarTypeUiModel>
    }

    interface Remote {
        fun getCarTypes(id:String?, pageNumber:Int, pageSize:Int): Single<CarDataResponse>
    }
}