package hossein.gheisary.carshow.ui.car.mappers

import hossein.gheisary.data.remote.model.CarDataResponse
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import javax.inject.Inject

class ManufactureMapper @Inject constructor():
    EntityMapper<CarDataResponse, List<ManufactureUiModel>> {
    override fun map(info: CarDataResponse): List<ManufactureUiModel> {
        return  info.wkda.map { ManufactureUiModel(it.key, it.value) }.toMutableList()
    }
}