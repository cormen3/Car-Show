package hossein.gheisary.carshow.ui.car

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import hossein.gheisary.data.remote.core.NetworkState

data class CarDataHolder<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit)