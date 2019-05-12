package hossein.gheisary.carshow.ui.car.manufacture.paging

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureDataStore
import hossein.gheisary.carshow.utility.extensions.performOnBackOutOnMain
import hossein.gheisary.carshow.ui.car.mappers.ManufactureMapper
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.data.remote.core.Scheduler
import java.util.concurrent.Executor

class ManufactureItemKeyedDataSource(
    private val remote: ManufactureDataStore.Remote,
    private val scheduler: Scheduler,
    private val manufactureMapper: ManufactureMapper,
    private val retryExecutor: Executor) : ItemKeyedDataSource<String, ManufactureUiModel>() {

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

    private var pageSize = 0

    override fun getKey(item: ManufactureUiModel): String = ""

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<ManufactureUiModel>) {
        remote.getManufactures(pageSize, params.requestedLoadSize)
            .performOnBackOutOnMain(scheduler)
            .doOnSubscribe { networkState.postValue(NetworkState.LOADING); initialLoad.postValue(NetworkState.LOADING)}
            .map { manufactureMapper.map(it) }
            .subscribe(
                {response ->
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(response!!)
                    pageSize++
                },
                {
                    retry = {loadInitial(params, callback)}
                    val error = NetworkState.error(it.message ?: "unknown error")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<ManufactureUiModel>) {
        remote.getManufactures(pageSize, params.requestedLoadSize)
            .performOnBackOutOnMain(scheduler)
            .doOnSubscribe { networkState.postValue(NetworkState.LOADING)}
            .map { manufactureMapper.map(it) }
            .subscribe(
                {response ->
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(response!!)
                    pageSize++
                },
                {   retry = {loadAfter(params, callback)}
                    val error = NetworkState.error(it.message ?: "unknown error")
                    networkState.postValue(error)
                })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<ManufactureUiModel>){}
}