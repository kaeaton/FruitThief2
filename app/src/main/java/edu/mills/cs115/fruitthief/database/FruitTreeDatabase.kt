package edu.mills.cs115.fruitthief.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.runBlocking

@Database(entities = [Fruit::class, Tree::class], version = 1, exportSchema = false)
abstract class FruitTreeDatabase : RoomDatabase() {
    abstract val fruitTreeDAO: FruitTreeDAO

    companion object {
        @Volatile
        private var INSTANCE: FruitTreeDatabase? = null

        /**
         *
         * @param context app Context
         * @return app's instance of the database
         */

        fun getInstance(context: Context): FruitTreeDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FruitTreeDatabase::class.java,
                        "fruit_tree_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    runBlocking {
                        populateFruitTable(instance)
                        populateTreeTable(instance)
                    }
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        suspend fun populateFruitTable(database: FruitTreeDatabase) {
            listOf(
                "Apple" to "HIJK", "Apricot" to "FG", "Asian Pear" to "IJK",
                "Avocado" to "FGH", "Blackberry Bush" to "GHI", "Fig" to "IJFG",
                "Grapefruit" to "LABCD", "Lemon" to "ABCDEFGHIJKL", "Lime" to "KLABCDEFG",
                "Loquat" to "EFG", "Mandarin Orange" to "KLAB", "Orange" to "LABCD",
                "Peach" to "GH", "Pear" to "HI", "Persimmon" to "J", "Pineapple Guava" to "JK",
                "Plum" to "FGHI", "Pluot" to "FGHI", "Pomegranate" to "JK", "Pomelo" to "KLAB",
                "Tangerine" to "KLAB", "Unknown" to "ABCDEFGHIJKL"
            ).forEach {
                database.fruitTreeDAO.insert(Fruit(fruitName = it.first, fruitSeason = it.second))
            }
        }

        // For testing purposes only
        suspend fun populateTreeTable(database: FruitTreeDatabase) {
            database.fruitTreeDAO.insert(Tree(0, 7, 37.892861, -122.078400))
            database.fruitTreeDAO.insert(Tree(0, 1, 37.870098, -122.070084))
            database.fruitTreeDAO.insert(Tree(0, 0, 37.897702, -122.083549))
        }
    }
}