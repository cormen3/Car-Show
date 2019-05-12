package hossein.gheisary.carshow.ui.car.manufacture.model

import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.data.remote.model.CarDataResponse
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class ManufactureDataStore {

    interface Repository {
        val refreshStatePublishSubject: PublishSubject<NetworkState>
        fun getData( pageSize: Int): CarDataHolder<ManufactureUiModel>
    }

    interface Remote {
        fun getManufactures(pageNumber:Int, pageSize:Int): Single<CarDataResponse>
    }
}