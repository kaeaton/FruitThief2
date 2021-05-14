package edu.mills.cs115.fruitthief.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mills.cs115.fruitthief.database.FruitTreeDAO
import java.lang.IllegalArgumentException

class MarkerViewModelFactory(
    private val dataSource: FruitTreeDAO
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarkerViewModel::class.java)) {
            return MarkerViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}