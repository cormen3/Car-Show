package hossein.gheisary.carshow.ui.car.manufacture.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureDataStore
import hossein.gheisary.carshow.ui.car.mappers.ManufactureMapper
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import hossein.gheisary.data.remote.core.Scheduler
import java.util.concurrent.Executor


class ManufactureDataSourceFactory(
    private val remote: ManufactureDataStore.Remote,
    private val scheduler: Scheduler,
    private val manufactureMapper: ManufactureMapper,
    private val retryExecutor: Executor
) : DataSource.Factory<String, ManufactureUiModel>() {
    val sourceLiveData = MutableLiveData<ManufactureItemKeyedDataSource>()

    override fun create(): DataSource<String, ManufactureUiModel> {
        val source = ManufactureItemKeyedDataSource(remote, scheduler, manufactureMapper, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}

