package hossein.gheisary.carshow.ui.car.type.paging

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import hossein.gheisary.carshow.ui.car.type.model.CarTypeDataStore
import hossein.gheisary.carshow.utility.extensions.performOnBackOutOnMain
import hossein.gheisary.carshow.ui.car.mappers.CarTypeMapper
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.carshow.ui.car.type.CarTypeUiModel
import java.util.concurrent.Executor

class CarTypeItemKeyedDataSource(
    private val remote: CarTypeDataStore.Remote,
    private val scheduler: Scheduler,
    private val carTypeMapper: CarTypeMapper,
    private val retryExecutor: Executor,
    private val searchKey: String?
) : ItemKeyedDataSource<String, CarTypeUiModel>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    private var pageNumber = 0

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<CarTypeUiModel>) {
        remote.getCarTypes(searchKey, pageNumber,params.requestedLoadSize)
            .performOnBackOutOnMain(scheduler)
            .doOnSubscribe { networkState.postValue(NetworkState.LOADING)}
            .map { carTypeMapper.map(it) }
            .subscribe(
                {response ->
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(response!!)
                    pageNumber++
                },
                {   retry = {loadAfter(params, callback)}
                    val error = NetworkState.error(it.message ?: "unknown error")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                })
    }

    override fun getKey(item: CarTypeUiModel): String = ""

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<CarTypeUiModel>) {
        remote.getCarTypes(searchKey, pageNumber,params.requestedLoadSize)
            .performOnBackOutOnMain(scheduler)
            .doOnSubscribe { networkState.postValue(NetworkState.LOADING); initialLoad.postValue(NetworkState.LOADING)}
            .map { carTypeMapper.map(it) }
            .subscribe(
                {response ->
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(response!!)
                    pageNumber++
                },
                {
                    retry = {loadInitial(params, callback)}
                    val error = NetworkState.error(it.message ?: "unknown error")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<CarTypeUiModel>){}
}