package hossein.gheisary.carshow.ui.car.year.model

import android.util.Log
import hossein.gheisary.carshow.utility.extensions.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import hossein.gheisary.data.remote.core.Scheduler
import hossein.gheisary.data.remote.exception.ExceptionHandler
import hossein.gheisary.data.remote.model.CarYearResponse
import hossein.gheisary.data.remote.model.Outcome

class CarYearRepository (
    private val remote: CarYearDataStore.Remote,
    private val scheduler: Scheduler,
    private val exceptionHandler: ExceptionHandler,
    private val compositeDisposable: CompositeDisposable
): CarYearDataStore.Repository {
    override val carYearOutcome: PublishSubject<Outcome<CarYearResponse>> = PublishSubject.create<Outcome<CarYearResponse>>()

    override fun getCarYearData(id:String?, type:String?) {
        remote.getCarYearData(id, type)
                .performOnBackOutOnMain(scheduler)
                .doOnSubscribe{carYearOutcome.loading(true)}
                .subscribe(
                        {loginInfo -> Log.i("dsafdsaf", "data come"); carYearOutcome.loading(false); carYearOutcome.success(loginInfo)},
                        {error -> carYearOutcome.loading(false); handleError(error)}
                )
                .addTo(compositeDisposable)
    }

    override fun handleError(error: Throwable) {
        val exception = exceptionHandler.handleError(error)
        carYearOutcome.failed(exception)
    }
}