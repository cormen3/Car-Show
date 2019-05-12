package hossein.gheisary.data.remote.core

import hossein.gheisary.data.remote.core.ApiRepository.Companion.API_KEY
import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.data.remote.model.CarYearResponse
import io.reactivex.Single
import retrofit2.http.*

interface  Restapi {

    @Headers("Content-Type: application/json")
    @GET("v1/car-types/manufacturer")
    fun getManufactures(@Query("page") page: Int,
                        @Query("pageSize") pageSize: Int,
                        @Query("wa_key") wa_key: String = API_KEY
    ): Single<CarDataResponse>


    @Headers("Content-Type: application/json")
    @GET("v1/car-types/built-dates")
    fun  getCarYearData(@Query("manufacturer") id: String?,
                        @Query("main-type") type: String?,
                        @Query("wa_key") wa_key: String = API_KEY
    ): Single<CarYearResponse>

    @Headers("Content-Type: application/json")
    @GET("v1/car-types/main-types")
    fun getCarTypes(@Query("manufacturer") id: String?,
                      @Query("page") page: Int,
                        @Query("pageSize") pageSize: Int,
                        @Query("wa_key") wa_key: String = API_KEY
    ): Single<CarDataResponse>


}

