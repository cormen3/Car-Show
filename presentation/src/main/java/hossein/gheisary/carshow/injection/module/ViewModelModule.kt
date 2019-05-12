package hossein.gheisary.carshow.injection.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import hossein.gheisary.carshow.injection.ViewModelFactory

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}