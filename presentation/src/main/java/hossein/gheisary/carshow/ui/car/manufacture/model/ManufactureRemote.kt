package hossein.gheisary.carshow.ui.car.manufacture.model

import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.model.CarDataResponse
import io.reactivex.Single
import javax.inject.Inject

class ManufactureRemote @Inject constructor(private val restDataSource: RestDataSource):
    ManufactureDataStore.Remote {
    override fun getManufactures(pageNumber: Int, pageSize: Int): Single<CarDataResponse> {
        return restDataSource.getManufactures(pageNumber, pageSize)
    }
}