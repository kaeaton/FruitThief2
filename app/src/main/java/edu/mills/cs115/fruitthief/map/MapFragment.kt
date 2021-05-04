package edu.mills.cs115.fruitthief.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.mills.cs115.fruitthief.R
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import edu.mills.cs115.fruitthief.database.Tree
import edu.mills.cs115.fruitthief.databinding.FragmentMapBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MarkerViewModel
    private lateinit var viewModelFactory: MarkerViewModelFactory
    private var mapReady = false
    private lateinit var treesToDisplay: LiveData<List<Tree>>
    private lateinit var trees: LiveData<List<Tree>>
    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = FruitTreeDatabase.getInstance(application).fruitTreeDAO
        viewModelFactory = MarkerViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MarkerViewModel::class.java)
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

//        trees = viewModel.allTrees
        treesToDisplay = viewModel.allTrees

        // Inflate the layout for this fragment
//        val binding = DataBindingUtil.inflate<FragmentMapBinding>(
//            inflater, R.layout.fragment_map, container, false)
//        var rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val binding = FragmentMapBinding.inflate(inflater)
//        binding.viewModel = viewModel
        mapViewModel.navigateToAddTree.observe(viewLifecycleOwner,
            Observer<Boolean> { navigate ->
                if (navigate) {
                    val navController = findNavController()
                    navController.navigate(R.id.action_mapFragment_to_addTreeFragment)
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
            mapViewModel.onFabClicked()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        treesToDisplay.observe(viewLifecycleOwner, Observer {
            trees = treesToDisplay
            if(mapReady) {
                updateMap()
            }
        })
    }

    private fun updateMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.892860, -122.078400)))
        trees.value?.forEach { tree ->
            val marker = LatLng(tree.lat, tree.lng)
            mMap.addMarker(
                MarkerOptions().position(marker).title(tree.fruit.toString())
            )
        }
    }

}
