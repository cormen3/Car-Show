package hossein.gheisary.carshow.ui.car.mappers

import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.carshow.ui.car.type.CarTypeUiModel
import javax.inject.Inject

class CarTypeMapper @Inject constructor():
    EntityMapper<CarDataResponse, List<CarTypeUiModel>> {
    override fun map(info: CarDataResponse): List<CarTypeUiModel> {
        return  info.wkda.map { CarTypeUiModel(it.key) }.toMutableList()
    }
}