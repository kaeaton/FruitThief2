package edu.mills.cs115.fruitthief.map

import android.app.Application
import androidx.lifecycle.*
import edu.mills.cs115.fruitthief.database.*
import kotlinx.coroutines.launch

class MarkerViewModel(
    val database: FruitTreeDAO,
    application: Application) : ViewModel() {

    var selectedFruit = "Lemon"

    lateinit var allFruit: Array<Fruit>
    lateinit var allTrees: LiveData<List<Tree>>
//    = liveData {
//         val data = database.getTreeList()
//        emit(data)
//    }

    val specificFruitTrees: LiveData<List<Tree>> = liveData {
        val data = database.filterByFruit(selectedFruit)
        emit(data)
    }

//    var currentFruitTrees = MutableLiveData<List<Tree?>>()

    init {
        // temporarily seeding the database
        PopulateTreeTable(database)
        PopulateFruitTable(database)

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
            var trees = database.getTreeList()
            emit(trees)
        }
    }

    private suspend fun getCurrentFruitTreesFromDatabase(): LiveData<List<Tree>> {
        return liveData {
            var trees = database.filterByFruit(selectedFruit)
            emit(trees)
        }
    }

    private suspend fun getAllFruitFromDatabase(): Array<Fruit> {
        return database.getFruitList()
    }

}