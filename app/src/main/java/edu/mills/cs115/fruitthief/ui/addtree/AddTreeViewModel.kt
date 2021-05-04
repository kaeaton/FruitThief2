package edu.mills.cs115.fruitthief.ui.addtree

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import edu.mills.cs115.fruitthief.database.Tree
import kotlinx.coroutines.runBlocking

class AddTreeViewModel : ViewModel() {
    var fruit = "Unknown"
    var loc = LatLng(0.0, 0.0)
    private val _navigateToAddTree = MutableLiveData<Boolean>()
    val navigateToAddTree: LiveData<Boolean>
        get() = _navigateToAddTree


    fun onItemSelected(string: String){
        fruit = string
    }

    fun onButtonClicked(dataSource: FruitTreeDAO, context: Context){
        runBlocking {
            dataSource
                .insert(Tree(0, dataSource.getFruitByName(fruit).fruitId, loc.latitude, loc.longitude))
        }
    }

    fun onFabClicked() {
        _navigateToAddTree.value = true
    }

    fun onNavigatedToFilter() {
        _navigateToAddTree.value =false
    }

    fun setLocation(location: LatLng) {
        loc = location
    }
}