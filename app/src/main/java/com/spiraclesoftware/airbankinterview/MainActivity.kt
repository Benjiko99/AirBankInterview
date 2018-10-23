package com.spiraclesoftware.airbankinterview

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main__activity.*

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main__activity)

        setSupportActionBar(toolbar)
        collapsingToolbar.setupWithNavController(toolbar, findNavController(R.id.navHostFragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null, findNavController(R.id.navHostFragment))
    }
}