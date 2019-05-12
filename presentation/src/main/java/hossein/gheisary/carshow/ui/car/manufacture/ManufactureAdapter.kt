package hossein.gheisary.carshow.ui.car.manufacture

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.ui.car.NetworkStateItemViewHolder
import hossein.gheisary.data.remote.core.NetworkState

class ManufactureAdapter(private val retryCallback: () -> Unit, private val onItemClick: (String?, String?) -> Unit)
    : PagedListAdapter<ManufactureUiModel, RecyclerView.ViewHolder>(ITEM_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_manufacture_even -> (holder as ManufactureViewHolder).bind(getItem(position))
            R.layout.item_manufacture_odd-> (holder as ManufactureViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder,position: Int, payloads: MutableList<Any>) {
       onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_manufacture_even -> ManufactureViewHolder.create(parent, onItemClick, R.layout.item_manufacture_even)
            R.layout.item_manufacture_odd -> ManufactureViewHolder.create(parent, onItemClick, R.layout.item_manufacture_odd)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else if(position % 2 == 0){
            R.layout.item_manufacture_even
        }else {
            R.layout.item_manufacture_odd
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ManufactureUiModel>() {
            override fun areContentsTheSame(oldItem: ManufactureUiModel, newItem: ManufactureUiModel): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ManufactureUiModel, newItem: ManufactureUiModel): Boolean =
                newItem.manufactureId == oldItem.manufactureId
        }
    }
}
