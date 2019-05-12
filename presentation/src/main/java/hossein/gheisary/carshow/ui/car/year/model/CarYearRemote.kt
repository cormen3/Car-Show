package hossein.gheisary.carshow.ui.car.year.model

import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.model.CarYearResponse
import io.reactivex.Single

class CarYearRemote(private val restDataSource: RestDataSource): CarYearDataStore.Remote {
    override fun getCarYearData(id: String?, type: String?): Single<CarYearResponse> {
        return  restDataSource.getCarYearData(id, type)
    }
}