package hossein.gheisary.carshow.list.model

import hossein.gheisary.data.remote.core.Restapi
import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.data.remote.model.CarYearResponse
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import io.reactivex.Single
import java.io.IOException

class FakeRestApi : Restapi {

    override fun getCarYearData(id: String?, type: String?, wa_key: String): Single<CarYearResponse>
    {return Single.just(CarYearResponse(mapOf()))}

    override fun getCarTypes(id: String?, page: Int, pageSize: Int, wa_key: String): Single<CarDataResponse>
    {return Single.just(CarDataResponse(0,0,0,mapOf()))}

    private val model = mutableMapOf<String, Manufacture>()
    var failureMsg: String? = null

    fun addPost(post: ManufactureUiModel?) {
        val manufactures = model.getOrPut("test") {Manufacture(items = arrayListOf())}
        manufactures.items.add(post!!)
    }

    fun clear() {
        model.clear()
    }

    private fun findPosts(pageSize: Int): Map<String, String> {
        val manufacture = findManufacture()
        val posts = manufacture.findPosts(pageSize)
        return posts.associateBy({it.manufactureId}, {it.manufactureName})
    }

    private fun findManufacture():FakeRestApi.Manufacture{
        return model.getOrDefault("test", Manufacture())
    }

    override fun getManufactures(page: Int, pageSize: Int, wa_key: String): Single<CarDataResponse> {
        failureMsg?.let { return Single.error(IOException(it)) }
        val items = findPosts(pageSize)
        val response = CarDataResponse(page, pageSize,0, items)
        return Single.just(response)
    }

    private class Manufacture(val items: MutableList<ManufactureUiModel> = arrayListOf()) {
        fun findPosts(pageSize: Int): List<ManufactureUiModel> {
                return items.subList(0, Math.min(items.size, pageSize))
        }
    }
}