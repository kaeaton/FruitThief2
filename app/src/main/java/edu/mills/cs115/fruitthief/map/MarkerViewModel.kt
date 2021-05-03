package edu.mills.cs115.fruitthief.map

import android.app.Application
import androidx.lifecycle.*
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import edu.mills.cs115.fruitthief.database.PopulateTreeTable
import edu.mills.cs115.fruitthief.database.Tree
import kotlinx.coroutines.launch

class MarkerViewModel(
    val database: FruitTreeDAO,
    application: Application) : ViewModel() {

    var selectedFruit = "Lemon"

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

        initializeCurrentFruitTrees()
    }

    private fun initializeCurrentFruitTrees() {
        viewModelScope.launch {
            allTrees = getCurrentFruitTreesFromDatabase()
        }
    }



    private suspend fun getCurrentFruitTreesFromDatabase(): LiveData<List<Tree>> {
        return liveData {
            var trees = database.getTreeList()
            emit(trees)
        }
    }

}