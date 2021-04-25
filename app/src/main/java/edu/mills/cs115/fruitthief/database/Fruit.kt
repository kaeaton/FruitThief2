package edu.mills.cs115.fruitthief.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Data table of fruit and their seasons. To be prepopulated when the user first opens the app.
 *
 * @constructor fruitName name of the fruit
 * @constructor fruit season represented by A-L as corresponds to Jan-Dec
 */
@Entity(tableName = "fruit_info_table")
data class Fruit (
    @PrimaryKey(autoGenerate = true)
    var fruitId: Int = 0,

    @ColumnInfo(name = "fruit_name")
    var fruitName: String,

    //A-L = Jan-Dec
    @ColumnInfo(name = "fruit_season")
    var fruitSeason: String
)