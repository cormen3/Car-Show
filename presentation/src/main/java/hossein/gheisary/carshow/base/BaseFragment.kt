package hossein.gheisary.carshow.base

import android.content.Context
import androidx.fragment.app.FragmentActivity
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import hossein.gheisary.carshow.utility.extensions.toInt
import kotlinx.android.synthetic.main.loading_view.*

abstract class BaseFragment : DaggerFragment() {
    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mActivity = activity as FragmentActivity
    }

    fun setLoading(isLoading: Boolean) {
        mActivity.mainLoadingRelativeLayout.visibility = isLoading.toInt()
    }
}