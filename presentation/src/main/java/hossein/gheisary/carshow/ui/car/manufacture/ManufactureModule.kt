package hossein.gheisary.carshow.ui.car.manufacture

import androidx.lifecycle.ViewModel
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureDataStore
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureRemote
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.carshow.injection.ViewModelKey
import hossein.gheisary.carshow.ui.car.manufacture.paging.ManufactureDataSourceFactory
import hossein.gheisary.carshow.ui.car.mappers.ManufactureMapper
import java.util.concurrent.Executor

@Module
abstract class ManufactureModule {
    @Binds
    @IntoMap
    @ViewModelKey(ManufactureViewModel::class)
    abstract fun bindManufactureViewModel(manufactureViewModel: ManufactureViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun provideManufactureRepository(
            ioExecutor: Executor,
            sourceFactory: ManufactureDataSourceFactory): ManufactureDataStore.Repository {
            return ManufactureRepository(ioExecutor, sourceFactory)
        }

        @JvmStatic
        @Provides
        internal fun provideManufactureRemote(restDataSource: RestDataSource): ManufactureDataStore.Remote {
            return ManufactureRemote(restDataSource)
        }

        @JvmStatic
        @Provides
        internal fun provideManufactureDataSourceFactory(
            remote: ManufactureDataStore.Remote,
            scheduler: Scheduler,
            manufactureMapper: ManufactureMapper,
            retryExecutor: Executor): ManufactureDataSourceFactory {
            return ManufactureDataSourceFactory(remote, scheduler, manufactureMapper, retryExecutor)
        }
        @JvmStatic
        @Provides
        internal fun provideManufactureMapper(): ManufactureMapper {
            return ManufactureMapper()
        }
    }
}