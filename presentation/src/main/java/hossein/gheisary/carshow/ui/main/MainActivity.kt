package hossein.gheisary.carshow.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import hossein.gheisary.carshow.R
import hossein.gheisary.carshow.utility.extensions.toInt
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() , HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?: MainNavigator(this).navigateToManufactureFragment()

        toolbarLogoBackImageView.setOnClickListener { onBackPressed() }
    }

    fun setToolBarTitle(title:String?, hasBack:Boolean){
        mainToolbarTitleTextView.text = title
        toolbarLogoBackImageView.visibility = hasBack.toInt()
    }
}
