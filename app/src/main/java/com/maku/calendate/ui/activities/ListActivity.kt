package com.maku.calendate.ui.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maku.calendate.R
import com.maku.calendate.databinding.ActivityListBinding
import com.maku.calendate.databinding.FragmentCalenderBinding
import com.maku.calendate.ui.fragments.list.PostBottomDialogFragment

class ListActivity : AppCompatActivity(), PostBottomDialogFragment.ItemClickListener {

    private lateinit var mActivityListBinding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.
        super.onCreate(savedInstanceState)

        mActivityListBinding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)

    }

    // menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Call for help change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            else -> true
        }

    }

    override fun onItemClick(item: String?) {
        TODO("Not yet implemented")
    }

}