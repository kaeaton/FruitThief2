package edu.mills.cs115.fruitthief.database

import timber.log.Timber

class PopulateTreeTable(fruitTreeDao: FruitTreeDAO) {

    init {
        if (fruitTreeDao.getFruitList().isEmpty()) {
            Timber.i("Populating tree table")
            fruitTreeDao.insert(Tree(0, 0, 37.892860, -122.078400))
            fruitTreeDao.insert(Tree(0, 0, 37.870098, -122.070084))
            fruitTreeDao.insert(Tree(0, 0, 37.897701, -122.083549))
        }
    }
}