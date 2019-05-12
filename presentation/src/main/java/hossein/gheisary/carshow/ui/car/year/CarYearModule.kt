package hossein.gheisary.carshow.ui.car.year

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.carshow.injection.ViewModelKey
import hossein.gheisary.carshow.ui.car.year.model.CarYearDataStore
import hossein.gheisary.carshow.ui.car.year.model.CarYearRemote
import hossein.gheisary.carshow.ui.car.year.model.CarYearRepository
import hossein.gheisary.data.remote.exception.ExceptionHandler
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class CarYearModule {
    @Binds
    @IntoMap
    @ViewModelKey(CarYearViewModel::class)
    abstract fun bindCarYearViewModel(carYearViewModel: CarYearViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun provideCarTypeRepository(remote: CarYearDataStore.Remote,
                                              scheduler: Scheduler,
                                              exceptionHandler: ExceptionHandler,
                                              compositeDisposable: CompositeDisposable
        ): CarYearDataStore.Repository {
            return CarYearRepository(remote, scheduler, exceptionHandler,compositeDisposable)
        }

        @JvmStatic
        @Provides
        internal fun provideCarTypeRemote(restDataSource: RestDataSource): CarYearDataStore.Remote {
            return CarYearRemote(restDataSource)
        }
    }
}