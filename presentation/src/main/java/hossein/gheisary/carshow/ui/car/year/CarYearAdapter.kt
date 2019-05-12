package hossein.gheisary.carshow.ui.car.year

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import hossein.gheisary.carshow.R
import kotlinx.android.synthetic.main.item_car_year.view.*

class CarYearAdapter : RecyclerView.Adapter<ViewHolderCarYear>() {
    private lateinit var items : List<String>

    private val clickSubject = PublishSubject.create<String>()
    val clickEvent : Observable<String> = clickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarYear {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_year, parent, false)
        return ViewHolderCarYear(view)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolderCarYear, position: Int) {
        holder.carYearItemTextView.text =  items[position]
        holder.carYearConstraintLayout.setOnClickListener {  clickSubject.onNext(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items : List<String>){
        this.items = items
    }
}

class ViewHolderCarYear(view: View) : RecyclerView.ViewHolder(view) {
    val carYearItemTextView = view.carYearItemTextView!!
    val carYearConstraintLayout = view.carYearConstraintLayout!!
}



