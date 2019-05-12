package hossein.gheisary.carshow.ui.car.type

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.carshow.injection.ViewModelKey
import hossein.gheisary.carshow.ui.car.type.model.CarTypeDataStore
import hossein.gheisary.carshow.ui.car.type.model.CarTypeRemote
import hossein.gheisary.carshow.ui.car.type.model.CarTypeRepository
import hossein.gheisary.carshow.ui.car.type.paging.CarTypeDataSourceFactory
import hossein.gheisary.carshow.ui.car.mappers.CarTypeMapper
import java.util.concurrent.Executor

@Module
abstract class CarTypeModule {
    @Binds
    @IntoMap
    @ViewModelKey(CarTypeViewModel::class)
    abstract fun bindCarTypeViewModel(CarTypeViewModel: CarTypeViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun provideCarTypeRepository(
            ioExecutor: Executor,
            sourceFactory: CarTypeDataSourceFactory): CarTypeDataStore.Repository {
            return CarTypeRepository(ioExecutor, sourceFactory)
        }

        @JvmStatic
        @Provides
        internal fun provideCarTypeRemote(restDataSource: RestDataSource): CarTypeDataStore.Remote {
            return CarTypeRemote(restDataSource)
        }

        @JvmStatic
        @Provides
        internal fun provideCarTypeDataSourceFactory(
            remote: CarTypeDataStore.Remote,
            scheduler: Scheduler,
            carTypeMapper: CarTypeMapper,
            retryExecutor: Executor): CarTypeDataSourceFactory {
            return CarTypeDataSourceFactory(remote, scheduler, carTypeMapper, retryExecutor)
        }
        @JvmStatic
        @Provides
        internal fun provideCarTypeMapper(): CarTypeMapper {
            return CarTypeMapper()
        }
    }
}