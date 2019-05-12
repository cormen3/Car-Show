package hossein.gheisary.carshow.ui.car.year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.base.BaseFragment
import hossein.gheisary.carshow.ui.main.MainActivity
import hossein.gheisary.carshow.utility.extensions.doKeepDialog
import hossein.gheisary.data.remote.model.CarYearResponse
import hossein.gheisary.data.remote.model.Outcome
import kotlinx.android.synthetic.main.fragment_car_year.*
import javax.inject.Inject

class CarYearFragment:BaseFragment() {
    companion object {
        const val ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_ID = "ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_ID"
        const val ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_NAME = "ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_NAME"
        const val ARG_FRAGMENT_CAR_YEAR_CAR_TYPE = "ARG_FRAGMENT_CAR_YEAR_CAR_TYPE"
        fun newInstance(args: Bundle) =  CarYearFragment().apply { arguments = args }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var carYearViewModel: CarYearViewModel

    lateinit var carYearAdapter : CarYearAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_car_year, null)
    }

    override fun onResume() {
        super.onResume()
        (mActivity as MainActivity).setToolBarTitle(carYearViewModel.manufactureName + " , " + carYearViewModel.carType, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carYearViewModel = ViewModelProviders.of(this, viewModelFactory).get(CarYearViewModel::class.java)

        initObservers()

        setupList()

        prepareUiFeed()
    }

    private fun prepareUiFeed() {
        if(carYearViewModel.yearsData!=null){
            updateUI(carYearViewModel.yearsData!!)
        }else{
            carYearViewModel.manufactureName = arguments?.getString(ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_NAME)
            carYearViewModel.carType = arguments?.getString(ARG_FRAGMENT_CAR_YEAR_CAR_TYPE)
            carYearViewModel.carId = arguments?.getString(ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_ID)

            carYearViewModel.getCarYearData(carYearViewModel.carId, carYearViewModel.carType)
        }
    }

    private fun initObservers() {
        carYearViewModel.activeWorksDetailOutcome.observe(this, Observer<Outcome<CarYearResponse>> {outcome ->
            when (outcome) {
                is Outcome.Progress -> { setLoading(outcome.loading)}

                is Outcome.Success -> { carYearViewModel.yearsData = outcome.data; updateUI(outcome.data) }

                is Outcome.Failure -> {Toast.makeText(context,outcome.exception.message, Toast.LENGTH_LONG).show()}}
        })
    }

    private fun setupList() {
        val linearLayoutManager =  LinearLayoutManager(mActivity)
        carYearRecyclerView.layoutManager = linearLayoutManager
        carYearRecyclerView.setHasFixedSize(true)
        carYearAdapter= CarYearAdapter()
        carYearAdapter.clickEvent.subscribe{showSummaryDialog(it)}
    }

    private fun showSummaryDialog(year:String) {
        AlertDialog.Builder(mActivity)
                    .setTitle(getString(R.string.dialog_title_manufacture)+ carYearViewModel.manufactureName)
                    .setMessage(getString(R.string.dialog_message_available_model) +carYearViewModel.carType
                         + "\n" + "\n" + getString(R.string.dialog_message_made_in) +year)
                    .setPositiveButton("ok") {_,_->}
                    .show()
                    .doKeepDialog()

    }

    private fun updateUI(data: CarYearResponse) {
        carYearAdapter.setData(data.wkda.keys.toList())
        carYearRecyclerView.adapter = carYearAdapter
        carYearAdapter.notifyDataSetChanged()
    }
}

