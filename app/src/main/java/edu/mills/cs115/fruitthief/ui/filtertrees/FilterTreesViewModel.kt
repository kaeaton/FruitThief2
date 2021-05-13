package edu.mills.cs115.fruitthief.ui.filtertrees

import androidx.lifecycle.ViewModel
import edu.mills.cs115.fruitthief.database.FruitTreeDAO

class FilterTreesViewModel : ViewModel() {
    private val anyString = "Any"
    var fruit = anyString

    fun onItemSelected(fruitName: String) {
        fruit = fruitName
    }

    fun onButtonClicked(dataSource: FruitTreeDAO) {
        // TODO get data to map markers
    }
}