package hossein.gheisary.carshow.ui.car.manufacture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hossein.gheisary.carshow.R

class ManufactureViewHolder(view: View, private  val onItemClick: (String?, String?) -> Unit) : RecyclerView.ViewHolder(view) {
    private val itemManufactureNameTextView: TextView = view.findViewById(R.id.itemManufactureNameTextView)
    private var listItem : ManufactureUiModel? = null

    init {
        view.setOnClickListener {
           onItemClick(listItem?.manufactureId, listItem?.manufactureName)
        }
    }

    fun bind(listItem: ManufactureUiModel?) {
        this.listItem = listItem
        itemManufactureNameTextView.text = listItem?.manufactureName ?: "loading"
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (String?, String?) -> Unit, resourceId: Int): ManufactureViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(resourceId, parent, false)
            return ManufactureViewHolder(view, onItemClick)
        }
    }
}