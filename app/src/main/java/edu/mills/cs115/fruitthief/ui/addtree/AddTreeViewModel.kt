package edu.mills.cs115.fruitthief.ui.addtree

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import edu.mills.cs115.fruitthief.database.Tree
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AddTreeViewModel : ViewModel() {
    var fruit = "Unknown"
    private var loc = LatLng(0.0, 0.0)
    private val _navigateToAddTree = MutableLiveData<Boolean>()
    val navigateToAddTree: LiveData<Boolean>
        get() = _navigateToAddTree


    fun onItemSelected(string: String){
        fruit = string
    }

    fun onButtonClicked(dataSource: FruitTreeDAO){

        runBlocking {
            dataSource
                .insert(Tree(0, dataSource.getFruitByName(fruit).fruitId, loc.latitude, loc.longitude))
            Timber.i("tree table: %s", dataSource.getTreeList())
        }
        Timber.i("TreeViewModel current location: %s", loc.toString())
        Timber.i("TreeViewModel current latitude: %s", loc.latitude.toString())
        Timber.i("TreeViewModel current longitude: %s", loc.longitude.toString())
    }

    fun onFabClicked() {
        _navigateToAddTree.value = true
    }

    fun onNavigatedToFilter() {
        _navigateToAddTree.value =false
    }

    fun setLocation(lat: Double, lng: Double) {
        loc = LatLng(lat, lng)
    }
}