package edu.mills.cs115.fruitthief.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _navigateToAddTree = MutableLiveData<Boolean>()
    val navigateToAddTree: LiveData<Boolean>
        get() = _navigateToAddTree

    fun onFabClicked() {
        _navigateToAddTree.value = true
    }

    fun onNavigatedToFilter() {
        _navigateToAddTree.value =false
    }
}