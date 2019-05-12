package hossein.gheisary.carshow.ui.car.type.model

import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.model.CarDataResponse
import io.reactivex.Single
import javax.inject.Inject

class CarTypeRemote @Inject constructor(private val restDataSource: RestDataSource):
    CarTypeDataStore.Remote {
    override fun getCarTypes(id:String?, pageNumber: Int, pageSize: Int): Single<CarDataResponse> {
        return restDataSource.getCarTypes(id, pageNumber, pageSize)
    }
}