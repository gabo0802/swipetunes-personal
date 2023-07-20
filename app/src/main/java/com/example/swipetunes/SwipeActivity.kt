package com.example.swipetunes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)





        val fragmentManager: FragmentManager = supportFragmentManager

        // define your fragments here
        val infoFragment: Fragment = infoFragment()
        val swipeFragment: Fragment = swipeFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_info -> fragment = infoFragment
                R.id.nav_swipe -> fragment = swipeFragment
            }
            fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_swipe

    }
}