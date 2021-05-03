package edu.mills.cs115.fruitthief.map

import android.app.Application
import androidx.lifecycle.*
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import edu.mills.cs115.fruitthief.database.Tree
import kotlinx.coroutines.launch

class MarkerViewModel(
    val database: FruitTreeDAO,
    application: Application) : ViewModel() {

    val allTrees = database.getTreeList()
    var selectedFruit = "Lemon"
    var currentFruitTrees = MutableLiveData<List<Tree?>>()

    init {
        initializeCurrentFruitTrees()
    }

    private fun initializeCurrentFruitTrees() {
        viewModelScope.launch {
            currentFruitTrees.value = getCurrentFruitTreesFromDatabase()
        }
    }

    private suspend fun getCurrentFruitTreesFromDatabase(): List<Tree?> {
        var trees = database.filterByFruit(selectedFruit)
//        if (trees.isEmpty()) {
//            trees = null
//        }
        return trees
    }

}