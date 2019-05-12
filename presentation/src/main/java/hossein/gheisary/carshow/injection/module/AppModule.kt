package hossein.gheisary.carshow.injection.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module(includes = [NetworkModule::class, BaseModule::class, ViewModelModule::class])
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: Application): Context

    @Module
    companion object {
        @JvmStatic
        @Provides
        internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
    }
}