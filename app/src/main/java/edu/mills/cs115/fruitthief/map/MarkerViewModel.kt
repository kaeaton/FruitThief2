package edu.mills.cs115.fruitthief.map

import androidx.lifecycle.*
import edu.mills.cs115.fruitthief.database.*
import kotlinx.coroutines.launch

class MarkerViewModel(
    val database: FruitTreeDAO) : ViewModel() {

    var selectedFruit = "Lemon"

    lateinit var allFruit: Array<Fruit>
    lateinit var allTrees: LiveData<List<Tree>>

    val specificFruitTrees: LiveData<List<Tree>> = liveData {
        val data = database.filterByFruit(selectedFruit)
        emit(data)
    }

    init {
        initializeCurrentFruitTrees()
        initializeAllFruit()
    }

    private fun initializeCurrentFruitTrees() {
        viewModelScope.launch {
            allTrees = getAllFruitTreesFromDatabase()
        }
    }

    private fun initializeAllFruit() {
        viewModelScope.launch {
            allFruit = getAllFruitFromDatabase()
        }
    }

    private suspend fun getAllFruitTreesFromDatabase(): LiveData<List<Tree>> {
        return liveData {
            val trees = database.getTreeList()
            emit(trees)
        }
    }

    private suspend fun getCurrentFruitTreesFromDatabase(): LiveData<List<Tree>> {
        return liveData {
            val trees = database.filterByFruit(selectedFruit)
            emit(trees)
        }
    }

    private suspend fun getAllFruitFromDatabase(): Array<Fruit> {
        return database.getFruitList()
    }

}