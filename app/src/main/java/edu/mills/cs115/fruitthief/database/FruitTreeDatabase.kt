package edu.mills.cs115.fruitthief.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.mills.cs115.fruitthief.R
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
                        populateFruitTable(instance, context)
                        populateTreeTable(instance)
                    }
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }

        suspend fun populateFruitTable(database: FruitTreeDatabase, context: Context) {
            listOf(
                context.getString(R.string.apple_text) to "HIJK",
                context.getString(R.string.apricot_text) to "FG",
                context.getString(R.string.asian_pear_text) to "IJK",
                context.getString(R.string.avocado_text) to "FGH",
                context.getString(R.string.blackberry_text) to "GHI",
                context.getString(R.string.fig_text) to "IJFG",
                context.getString(R.string.grapefruit_text) to "LABCD",
                context.getString(R.string.lemon_text) to "ABCDEFGHIJKL",
                context.getString(R.string.lime_text) to "KLABCDEFG",
                context.getString(R.string.loquat_text) to "EFG",
                context.getString(R.string.mandarin_orange_text) to "KLAB",
                context.getString(R.string.nectarine_text) to "GH",
                context.getString(R.string.orange_text) to "LABCD",
                context.getString(R.string.peach_text) to "GH",
                context.getString(R.string.pear_text) to "HI",
                context.getString(R.string.persimmon_text) to "J",
                context.getString(R.string.pineapple_guava_text) to "JK",
                context.getString(R.string.plum_text) to "FGHI",
                context.getString(R.string.pluot_text) to "FGHI",
                context.getString(R.string.pomegranate_text) to "JK",
                context.getString(R.string.pomelo_text) to "KLAB",
                context.getString(R.string.tangerine_text) to "KLAB",
                context.getString(R.string.unknown_type_text) to "ABCDEFGHIJKL"
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