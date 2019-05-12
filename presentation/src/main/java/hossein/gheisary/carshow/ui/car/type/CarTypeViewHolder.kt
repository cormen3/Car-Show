package hossein.gheisary.carshow.ui.car.type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hossein.gheisary.carshow.R

class CarTypeViewHolder(view: View, private val onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.itemCarTypeTitleTextView)
    private var item : CarTypeUiModel? = null

    init {
        view.setOnClickListener {
            onItemClick(item?.carModel!!)
        }
    }

    fun bind(post: CarTypeUiModel?) {
        this.item = post
        title.text = post?.carModel ?: "loading"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (String) -> Unit,
            resourceId: Int
        ): CarTypeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(resourceId, parent, false)
            return CarTypeViewHolder(view, onItemClick)
        }
    }
}