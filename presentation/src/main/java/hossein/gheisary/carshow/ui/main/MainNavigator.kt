package hossein.gheisary.carshow.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureFragment
import hossein.gheisary.carshow.ui.car.type.CarTypeFragment
import hossein.gheisary.carshow.ui.car.type.CarTypeFragment.Companion.ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_ID
import hossein.gheisary.carshow.ui.car.type.CarTypeFragment.Companion.ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_NAME
import hossein.gheisary.carshow.ui.car.year.CarYearFragment
import hossein.gheisary.carshow.ui.car.year.CarYearFragment.Companion.ARG_FRAGMENT_CAR_YEAR_CAR_TYPE
import hossein.gheisary.carshow.ui.car.year.CarYearFragment.Companion.ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_ID
import hossein.gheisary.carshow.ui.car.year.CarYearFragment.Companion.ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_NAME
import hossein.gheisary.carshow.utility.extensions.inTransaction

class MainNavigator (activity: AppCompatActivity){
    private var fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToManufactureFragment() {
    val fragment = ManufactureFragment.newInstance()
    val tag = fragment.javaClass.toString()

        fragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment, tag)
            .commit()
    }

    fun navigateToCarTypeFragment(manufactureId: String?, manufactureName: String?) {
        val args = Bundle()
        args.putString(ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_ID, manufactureId)
        args.putString(ARG_FRAGMENT_CAR_TYPE_MANUFACTURE_NAME, manufactureName)

        val fragment = CarTypeFragment.newInstance(args)
        val tag = fragment.javaClass.toString()

        fragmentManager.inTransaction {
            replace(R.id.mainFragmentContainer, fragment, tag)
        }
    }

    fun navigateToCarYearFragment(manufactureId: String?, manufactureName: String?, carType: String) {
        val args = Bundle()
        args.putString(ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_ID, manufactureId)
        args.putString(ARG_FRAGMENT_CAR_YEAR_MANUFACTURE_NAME, manufactureName)
        args.putString(ARG_FRAGMENT_CAR_YEAR_CAR_TYPE, carType)

        val fragment = CarYearFragment.newInstance(args)
        val tag = fragment.javaClass.toString()

        fragmentManager.inTransaction {
            replace(R.id.mainFragmentContainer, fragment, tag)
        }
    }
}