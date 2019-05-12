package hossein.gheisary.carshow.ui.car.manufacture

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
import kotlinx.android.synthetic.main.fragment_manufacture.*
import javax.inject.Inject

class ManufactureFragment:BaseFragment() {
    companion object {
        fun newInstance() =  ManufactureFragment()
    }

    private lateinit var adapter: ManufactureAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: ManufactureViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ManufactureViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_manufacture, null)
    }

    override fun onResume() {
        super.onResume()
        (mActivity as MainActivity).setToolBarTitle(getString(R.string.all_manufactures), false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
        initObservers()

        if(mainViewModel.carManufactures!=null){
            updateList(mainViewModel.carManufactures)
        }else{
            mainViewModel.getData()
        }
    }

    private fun setupList() {
        val linearLayoutManager =  LinearLayoutManager(mActivity)
        manufacturesRecyclerView.layoutManager = linearLayoutManager
        manufacturesRecyclerView.setHasFixedSize(true)
        adapter = ManufactureAdapter( mainViewModel::retry, this::onItemClicked )
        manufacturesRecyclerView.adapter = adapter
    }

    private fun onItemClicked(manufactureId:String?, manufactureName:String?){
        MainNavigator(mActivity as AppCompatActivity)
            .navigateToCarTypeFragment(manufactureId, manufactureName)
    }

    private fun initObservers() {
        mainViewModel.items.observe(this, Observer<PagedList<ManufactureUiModel>> { updateList(it)})
        mainViewModel.networkState.observe(this, Observer { adapter.setNetworkState(it) })

        mainViewModel.refreshState.observe(this, Observer {
            if(it==NetworkState.LOADED) manufactureListSwipeRefreshLayout.isRefreshing = false
        })

        manufactureListSwipeRefreshLayout.setOnRefreshListener { mainViewModel.refresh() }
    }

    private fun updateList(it: PagedList<ManufactureUiModel>?) {
        mainViewModel.carManufactures = it
        adapter.submitList(mainViewModel.carManufactures)
    }


}
