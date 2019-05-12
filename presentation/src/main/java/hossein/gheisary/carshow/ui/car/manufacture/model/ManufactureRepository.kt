package hossein.gheisary.carshow.ui.car.manufacture.model

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.manufacture.paging.ManufactureDataSourceFactory
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import hossein.gheisary.data.remote.core.*
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor
import javax.inject.Inject

class ManufactureRepository @Inject constructor(private val ioExecutor: Executor, private val sourceFactory: ManufactureDataSourceFactory
) : ManufactureDataStore.Repository {
    override val refreshStatePublishSubject: PublishSubject<NetworkState> = PublishSubject.create<NetworkState>()

    override fun getData(pageSize: Int): CarDataHolder<ManufactureUiModel> {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
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


