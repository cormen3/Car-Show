package hossein.gheisary.carshow.ui.car.year.model

import hossein.gheisary.data.remote.model.CarYearResponse
import hossein.gheisary.data.remote.model.Outcome
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class CarYearDataStore {
    interface Repository {
        val carYearOutcome: PublishSubject<Outcome<CarYearResponse>>
        fun getCarYearData(id:String?, type:String?)
        fun handleError(error: Throwable)
    }

    interface Remote {
        fun getCarYearData(id:String?, type:String?) : Single<CarYearResponse>
    }
}