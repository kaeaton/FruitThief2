package edu.mills.cs115.fruitthief.database

import androidx.room.*

/**
 * DAO Database containing two tables, fruit_info_table and tree_info_table.
 *
 * @see Tree for tree_info_table
 * @see Fruit for fruit_info_table
 */
@Dao
interface FruitTreeDAO {
    /**
     * Function to insert a Tree entity into tree_info_table.
     *
     * @param tree New tree to be added.
     */
    @Insert
    fun insert(tree: Tree)

    /**
     * Function to insert a Fruit entity into fruit_info_table.
     * fruit_info_table is prepopulated, so the user should never access it.
     *
     * @param fruit Fruit to be added.
     */
    @Insert
    fun insert(fruit: Fruit)

    /**
     * Function to update a Tree's info.
     *
     * @param tree Tree with updated information.
     */
    @Update
    suspend fun update(tree: Tree)

    @Delete
    suspend fun delete(tree: Tree)

    @Query("DELETE FROM tree_info_table")
    suspend fun clearTrees()

    @Query("DELETE FROM fruit_info_table")
    suspend fun clear()

    /**
     * @return Array of all Fruit
     */
    @Query("SELECT * FROM fruit_info_table")
    fun getFruitList(): Array<Fruit>

    /**
     * @return Array of all Trees
     */
    @Query("SELECT * FROM tree_info_table")
    suspend fun getTreeList(): Array<Tree>

    /**
     *
     * @param fruitName String of fruit name
     * @return Fruit that matches given string
     */
    @Query("SELECT * FROM fruit_info_table WHERE fruit_name = :fruitName")
    fun getFruitByName(fruitName: String): Fruit

    /**
     *
     * @param fruit String specifying fruit type
     * @return Array of Trees with fruit matching given type
     */
    @Query("SELECT tree.* FROM tree_info_table tree INNER JOIN fruit_info_table ON fruit_type = fruitId WHERE fruit_name = :fruit")
    suspend fun filterByFruit(fruit: String): Array<Tree>

    /**
     *
     * @param month String of a single character representing the current month. A-l = Jan-Dec.
     * @return Array of all Trees whose fruit is currently in season
     */
    @Query("SELECT tree.* FROM tree_info_table tree INNER JOIN fruit_info_table ON fruit_type = fruitId WHERE fruit_season LIKE '%'+:month+'%'")
    suspend fun getInSeasonTrees(month: String): Array<Tree>

    @Query("SELECT fruit_name FROM fruit_info_table")
    fun getFruitNamesList(): Array<String>
}