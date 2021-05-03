package edu.mills.cs115.fruitthief.ui.filtertrees

import androidx.lifecycle.ViewModel
import edu.mills.cs115.fruitthief.database.FruitTreeDAO

class FilterTreesViewModel : ViewModel() {
    var fruit = "Any"

    fun onItemSelected(string: String){
        fruit = string
    }

    fun onButtonClicked(dataSource: FruitTreeDAO){
        // TODO get data to map markers
    }
}