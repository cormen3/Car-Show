package hossein.gheisary.data.remote.core

import io.reactivex.Single
import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.data.remote.model.CarYearResponse

class  RestDataSource constructor(var restapi: Restapi): ApiRepository {
    override fun getManufactures(pageNumber:Int, pageSize:Int): Single<CarDataResponse> {
        return restapi.getManufactures(pageNumber,pageSize)
    }

    override fun getCarYearData(id: String?, type: String?): Single<CarYearResponse> {
        return restapi.getCarYearData(id, type)
    }

    override fun getCarTypes(id:String?, pageNumber:Int, pageSize:Int): Single<CarDataResponse> {
        return restapi.getCarTypes(id, pageNumber,pageSize)
    }
}