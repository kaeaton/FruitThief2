package edu.mills.cs115.fruitthief.map

import android.app.Application
import androidx.lifecycle.*
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import edu.mills.cs115.fruitthief.database.Tree
import kotlinx.coroutines.launch

class MarkerViewModel(
    val database: FruitTreeDAO,
    application: Application) : ViewModel() {

    val allTrees: LiveData<List<Tree>> = liveData {
        val data = database.getTreeList()
        emit(data)
    }

    val specificFruitTrees: LiveData<List<Tree>> = liveData {
        val data = database.filterByFruit("Lemon")
        emit(data)
    }

    var selectedFruit = "Lemon"
    var currentFruitTrees = MutableLiveData<List<Tree?>>()

    init {
    }

//    private fun initializeCurrentFruitTrees() {
//        viewModelScope.launch {
//            currentFruitTrees.value = getCurrentFruitTreesFromDatabase()
//        }
//    }



//    private suspend fun getCurrentFruitTreesFromDatabase(): LiveData<List<Tree?>> {
//        liveData {
//        var trees = database.getTreeList()
////        if (trees.isEmpty()) {
////            trees = null
////        }
//        return trees
//    }

}