package hossein.gheisary.carshow.ui.car.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.base.BaseFragment
import hossein.gheisary.carshow.ui.main.MainActivity
import hossein.gheisary.carshow.ui.main.MainNavigator
import hossein.gheisary.data.remote.core.NetworkState
import kotlinx.android.synthetic.main.fragment_car_type.*
import javax.inject.Inject

class CarTypeFragment:BaseFragment() {
    companion object {
        const val ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_ID = "ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_ID"
        const val ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_NAME = "ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_NAME"
        fun newInstance(args: Bundle) =  CarTypeFragment().apply { arguments = args }
    }

    private lateinit var adapter: CarTypeAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var carTypeViewModel: CarTypeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_car_type, null)
    }

    override fun onResume() {
        super.onResume()
        (mActivity as MainActivity).setToolBarTitle(carTypeViewModel.manufactureName, true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carTypeViewModel = ViewModelProviders.of(this, viewModelFactory).get(CarTypeViewModel::class.java)

        setupList()
        initObservers()

        if(carTypeViewModel.carModels!=null){
            updateList(carTypeViewModel.carModels)
        }else{
            carTypeViewModel.manufactureId = arguments?.getString(ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_ID)
            carTypeViewModel.manufactureName = arguments?.getString(ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_NAME)
            carTypeViewModel.getData(carTypeViewModel.manufactureId)
        }
    }

    private fun setupList() {
        val linearLayoutManager =  LinearLayoutManager(mActivity)
        carTypeRecyclerView.layoutManager = linearLayoutManager
        adapter = CarTypeAdapter(this::onItemClicked,   carTypeViewModel::retry)
        carTypeRecyclerView.adapter = adapter
    }

    private fun onItemClicked(carType:String){
        MainNavigator(mActivity as AppCompatActivity).navigateToCarYearFragment(
            carTypeViewModel.manufactureId, carTypeViewModel.manufactureName, carType)
    }

    private fun initObservers() {
        carTypeViewModel.items.observe(this, Observer<PagedList<CarTypeUiModel>> { updateList(it) })
        carTypeViewModel.networkState.observe(this, Observer { adapter.setNetworkState(it) })

        carTypeViewModel.refreshState.observe(this, Observer {
            if(it==NetworkState.LOADED) carTypeListSwipeRefreshLayout.isRefreshing = false
        })

        carTypeListSwipeRefreshLayout.setOnRefreshListener { carTypeViewModel.refresh() }
    }

    private fun updateList(it: PagedList<CarTypeUiModel>?) {
        carTypeViewModel.carModels = it
        adapter.submitList(carTypeViewModel.carModels)
    }
}
