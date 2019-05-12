package hossein.gheisary.carshow.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hossein.gheisary.carshow.ui.main.MainActivity
import hossein.gheisary.carshow.ui.main.MainFragmentProvider
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [ManufactureModule::class, MainFragmentProvider::class])
    abstract fun bindMainActivity(): MainActivity
}