package hossein.gheisary.carshow.ui.car.manufacture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.base.BaseFragment
import hossein.gheisary.carshow.ui.main.MainActivity
import hossein.gheisary.carshow.ui.main.MainNavigator
import hossein.gheisary.carshow.utility.extensions.createViewModel
import hossein.gheisary.carshow.utility.extensions.observe
import hossein.gheisary.data.remote.core.NetworkState
import kotlinx.android.synthetic.main.fragment_manufacture.*

class ManufactureFragment:BaseFragment() {
    companion object {
        fun newInstance() =  ManufactureFragment()
    }

    private lateinit var adapter: ManufactureAdapter

    private lateinit var viewModel: ManufactureViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_manufacture, null)
    }

    override fun onResume() {
        super.onResume()
        (mActivity as MainActivity).setToolBarTitle(getString(R.string.all_manufactures), false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = createViewModel(viewModelFactory)
        setupList()
        initObservers()

        if(viewModel.carManufactures!=null){
            updateList(viewModel.carManufactures)
        }else{
            viewModel.getData()
        }
    }

    private fun setupList() {
        val linearLayoutManager =  LinearLayoutManager(mActivity)
        manufacturesRecyclerView.layoutManager = linearLayoutManager
        manufacturesRecyclerView.setHasFixedSize(true)
        adapter = ManufactureAdapter( viewModel::retry, this::onItemClicked )
        manufacturesRecyclerView.adapter = adapter
    }

    private fun onItemClicked(manufactureId:String?, manufactureName:String?){
        MainNavigator(mActivity as AppCompatActivity)
            .navigateToCarTypeFragment(manufactureId, manufactureName)
    }

    private fun initObservers() {
        observe(viewModel.items, ::updateList)
        observe(viewModel.networkState, ::setNetworkState)
        observe(viewModel.refreshState, ::setRefreshState)

        manufactureListSwipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun updateList(it: PagedList<ManufactureUiModel>?) {
        viewModel.carManufactures = it
        adapter.submitList(viewModel.carManufactures)
    }

    private fun setNetworkState(newNetworkState: NetworkState?){
        adapter.setNetworkState(newNetworkState)
    }
    private fun setRefreshState(newNetworkState: NetworkState?){
        if(newNetworkState==NetworkState.LOADED) manufactureListSwipeRefreshLayout.isRefreshing = false
    }

}
