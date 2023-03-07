package com.thallo.stage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.thallo.stage.databinding.ActivityHolderBinding
import com.thallo.stage.utils.StatusUtils

class HolderActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusUtils.init(this)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_holder)
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_graph2)
        when (intent.getStringExtra("Page")) {
            "DOWNLOAD" -> navGraph.setStartDestination(R.id.downloadFragment)
            "ADDONS" -> navGraph.setStartDestination(R.id.addonsManagerFragment)
            "SETTINGS" -> navGraph.setStartDestination(R.id.settingsFragment)
        }
        navController.graph=navGraph
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_holder)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}