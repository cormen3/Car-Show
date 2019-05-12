package hossein.gheisary.carshow.injection.module

import dagger.Module
import hossein.gheisary.carshow.ui.car.type.CarTypeModule
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureModule
import hossein.gheisary.carshow.ui.car.year.CarYearModule

@Module(includes = [ ManufactureModule::class, CarTypeModule::class, CarYearModule::class])
abstract class BaseModule