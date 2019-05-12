package hossein.gheisary.carshow.ui.car.type.model

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.type.paging.CarTypeDataSourceFactory
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.carshow.ui.car.type.CarTypeUiModel
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor
import javax.inject.Inject

class CarTypeRepository @Inject constructor(private val ioExecutor: Executor, private val sourceFactory: CarTypeDataSourceFactory
) : CarTypeDataStore.Repository {
    override val refreshStatePublishSubject: PublishSubject<NetworkState> = PublishSubject.create<NetworkState>()

    override fun getData(id:String?, pageSize: Int): CarDataHolder<CarTypeUiModel> {

        sourceFactory.searchKey = id

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setPrefetchDistance(1)
            .build()

        val builder = LivePagedListBuilder(sourceFactory, config).setFetchExecutor(ioExecutor)
        val livePagedList = builder.build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return CarDataHolder(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            refreshState = refreshState,
            refresh = {sourceFactory.sourceLiveData.value?.invalidate()},
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() }
        )
    }
}