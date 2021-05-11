package edu.mills.cs115.fruitthief

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.*
import edu.mills.cs115.fruitthief.databinding.ActivityMainBinding
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import edu.mills.cs115.fruitthief.database.PopulateFruitTable
import edu.mills.cs115.fruitthief.database.PopulateTreeTable
import edu.mills.cs115.fruitthief.map.MapFragment
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() { //, OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = binding.drawerLayout
        val application = requireNotNull(this).application
        val dataSource = FruitTreeDatabase.getInstance(application).fruitTreeDAO

        // clear tables for testing
        runBlocking {
            // clear for testing
            dataSource.clearFruitTable()
            dataSource.clearTrees()
            PopulateFruitTable(dataSource)
            PopulateTreeTable(dataSource)
        }

        // GPS Permission
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        // Adding the map within the layout container (the drawer)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MapFragment>(R.id.fragment_container_view)
            }
        }

        setUpNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(binding.navView, navController)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)

//        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
//            if (destination.id != R.id.mapFragment) {
//                findViewById<FloatingActionButton>(R.id.fab).hide()
//            } else {
//                findViewById<FloatingActionButton>(R.id.fab).show()
//            }
//        }
    }
}