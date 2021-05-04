package edu.mills.cs115.fruitthief

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.*
import edu.mills.cs115.fruitthief.databinding.ActivityMainBinding
import edu.mills.cs115.fruitthief.map.MapFragment

class MainActivity : AppCompatActivity() { //, OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = binding.drawerLayout

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MapFragment>(R.id.fragment_container_view)
            }
        }

        setUpNavigation()

//        val mapFragment = SupportMapFragment.newInstance()
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.mapFragment, mapFragment)
//            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

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

//    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(0.0, 0.0))
//                .title("Marker")
//        )
//    }

}