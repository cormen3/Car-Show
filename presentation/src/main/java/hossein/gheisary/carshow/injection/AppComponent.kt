package hossein.gheisary.carshow.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import hossein.gheisary.carshow.injection.module.AppModule
import hossein.gheisary.carshow.utility.App
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
    interface AppComponent {
        @Component.Builder
        interface Builder {
            @BindsInstance
            fun application(application: Application): Builder

            fun build(): AppComponent
        }
        fun inject(app: App)
    }