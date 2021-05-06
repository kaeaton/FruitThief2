package edu.mills.cs115.fruitthief.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.mills.cs115.fruitthief.R
import edu.mills.cs115.fruitthief.database.Fruit
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import edu.mills.cs115.fruitthief.database.Tree
import edu.mills.cs115.fruitthief.databinding.FragmentMapBinding
import edu.mills.cs115.fruitthief.ui.addtree.AddTreeViewModel
import timber.log.Timber

class MapFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MarkerViewModel
    private lateinit var viewModelFactory: MarkerViewModelFactory
    private var mapReady = false
    private lateinit var treesToDisplay: LiveData<List<Tree>>
    private lateinit var trees: LiveData<List<Tree>>
    private lateinit var mapViewModel: AddTreeViewModel
    private lateinit var currentLocation: LatLng
    private var locationCoordinates = LatLng(37.804363, -122.271111) // Oakland
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = FruitTreeDatabase.getInstance(application).fruitTreeDAO
        viewModelFactory = MarkerViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MarkerViewModel::class.java)
        mapViewModel = ViewModelProvider(this).get(AddTreeViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        currentLocation = LatLng(0.0, 0.0)

        treesToDisplay = viewModel.allTrees
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

        val binding = FragmentMapBinding.inflate(inflater)

        mapViewModel.navigateToAddTree.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if (navigate) {
                    val bundle = bundleOf(
                        Pair("lat", currentLocation.latitude),
                        Pair("long", currentLocation.longitude))
                    findNavController().navigate(R.id.action_mapFragment_to_addTreeFragment, bundle)
                    mapViewModel.onNavigatedToFilter()
                }
            })

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
        }

        binding.fab.setOnClickListener { view ->

//            currentLocation()
//            mapViewModel.onFabClicked()
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                currentLocation = LatLng(location.latitude, location.longitude)
                                Timber.i("Current location: " + currentLocation.toString())
                                Timber.i("Current latitude: " + currentLocation.latitude.toString())
                                Timber.i("Current longitude: " + currentLocation.longitude.toString())


//                                mapViewModel.setLocation(currentLocation)
                                mapViewModel.onFabClicked()
                            }
                            // Got last known location. In some rare situations this can be null.
                        }
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        treesToDisplay.observe(viewLifecycleOwner, Observer {
            trees = treesToDisplay
            if (mapReady) {
                updateMap()
            }
        })
    }

    private fun updateMap() {
//        cameraLocation()
        trees.value?.forEach { tree ->
            val marker = LatLng(tree.lat, tree.lng)
            mMap.addMarker(
                MarkerOptions().position(marker).title(getFruitName(tree.fruit))
            )
        }
    }

    private fun getFruitName(fruitID: Int): String {
        viewModel.allFruit.forEach { fruit ->
            if (fruit.fruitId.equals(fruitID)) {
                return fruit.fruitName
            }
        }
        return "Unknown Type"
    }

    private fun currentLocation() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            currentLocation = LatLng(location.latitude, location.longitude)

                        }
                        // Got last known location. In some rare situations this can be null.
                    }
            }
        }
//        return locationCoordinates
    }

}
