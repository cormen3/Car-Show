package hossein.gheisary.data.remote.core

import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.data.remote.model.CarYearResponse
import io.reactivex.Single

interface ApiRepository {
    companion object {
            val BASE_URL: String get() = "http://api-aws-eu-qa-1.auto1-test.com/"
            val API_KEY: String get() = "coding-puzzle-client-449cc9d"
    }

    fun getManufactures(pageNumber:Int, pageSize:Int): Single<CarDataResponse>
    fun getCarTypes(id:String?, pageNumber: Int, pageSize: Int): Single<CarDataResponse>
    fun getCarYearData(id: String?, type: String?): Single<CarYearResponse>
}