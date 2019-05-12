package hossein.gheisary.carshow.ui.car.type.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hossein.gheisary.carshow.ui.car.type.model.CarTypeDataStore
import hossein.gheisary.carshow.ui.car.mappers.CarTypeMapper
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.carshow.ui.car.type.CarTypeUiModel
import java.util.concurrent.Executor

class CarTypeDataSourceFactory(
    private val remote: CarTypeDataStore.Remote,
    private val scheduler: Scheduler,
    private val carTypeMapper: CarTypeMapper,
    private val retryExecutor: Executor
) : DataSource.Factory<String, CarTypeUiModel>() {
    val sourceLiveData = MutableLiveData<CarTypeItemKeyedDataSource>()

    var searchKey :String? = ""
    override fun create(): DataSource<String, CarTypeUiModel> {
        val source = CarTypeItemKeyedDataSource(remote, scheduler, carTypeMapper, retryExecutor, searchKey)
        sourceLiveData.postValue(source)
        return source
    }
}

