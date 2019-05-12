package hossein.gheisary.carshow.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hossein.gheisary.carshow.ui.car.type.CarTypeFragment
import hossein.gheisary.carshow.ui.car.type.CarTypeModule
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureFragment
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureModule
import hossein.gheisary.carshow.ui.car.year.CarYearFragment
import hossein.gheisary.carshow.ui.car.year.CarYearModule

@Module
abstract class MainFragmentProvider {
    @ContributesAndroidInjector(modules = [ManufactureModule::class])
    abstract fun provideManufactureFragment(): ManufactureFragment

    @ContributesAndroidInjector(modules = [CarTypeModule::class])
    abstract fun provideCarTypeFragment(): CarTypeFragment

    @ContributesAndroidInjector(modules = [CarYearModule::class])
    abstract fun provideCarYearFragment(): CarYearFragment
}