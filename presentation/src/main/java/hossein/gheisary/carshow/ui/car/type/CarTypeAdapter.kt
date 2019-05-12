package hossein.gheisary.carshow.ui.car.type

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.ui.car.NetworkStateItemViewHolder
import hossein.gheisary.data.remote.core.NetworkState

class CarTypeAdapter(private val onItemClick: (String) -> Unit, private val retryCallback: () -> Unit)
    : PagedListAdapter<CarTypeUiModel, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_car_type_odd -> (holder as CarTypeViewHolder).bind(getItem(position))
            R.layout.item_car_type_even -> (holder as CarTypeViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder,position: Int, payloads: MutableList<Any>) {
         onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_car_type_odd -> CarTypeViewHolder.create(parent, onItemClick, R.layout.item_car_type_odd)
            R.layout.item_car_type_even -> CarTypeViewHolder.create(parent, onItemClick, R.layout.item_car_type_even)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else if(position % 2 == 0){
            R.layout.item_car_type_even
        }else {
            R.layout.item_car_type_odd
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
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<CarTypeUiModel>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CarTypeUiModel, newItem: CarTypeUiModel): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CarTypeUiModel, newItem: CarTypeUiModel): Boolean =
                    oldItem.carModel == newItem.carModel
        }
    }
}
