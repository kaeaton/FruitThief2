package edu.mills.cs115.fruitthief.database

import androidx.room.*

/**
 * DAO interface for interacting with FruitTreeDatabase.
 *
 * @see [FruitTreeDatabase]
 * @see [Tree]
 * @see [Fruit]
 */
@Dao
interface FruitTreeDAO {
    /**
     * Inserts a tree into the database.
     *
     * @param tree tree to be added
     */
    @Insert
    suspend fun insert(tree: Tree)

    /**
     * Inserts a fruit into the database.
     *
     * @param fruit fruit to be added
     */
    @Insert
    suspend fun insert(fruit: Fruit)

    /**
     * Updates the tree information in the database.
     *
     * @param tree tree with updated information
     */
    @Update
    suspend fun update(tree: Tree)

    /**
     * Removes a tree from the database.
     *
     * @param tree tree to be deleted
     */
    @Delete
    suspend fun deleteTree(tree: Tree)

    /**
     * Clears all trees from the database.
     */
    @Query("DELETE FROM tree_info_table")
    suspend fun clearTrees()

    /**
     * Clears all fruit from the database.
     */
    @Query("DELETE FROM fruit_info_table")
    suspend fun clearFruitTable()

    /**
     * Get all fruits from the database.
     *
     * @return all fruits
     */
    @Query("SELECT * FROM fruit_info_table")
    suspend fun getFruitList(): Array<Fruit>

    /**
     * Get all trees from the database.
     *
     * @return all trees
     */
    @Query("SELECT * FROM tree_info_table")
    suspend fun getTreeList(): List<Tree>

    /**
     * Get information about the named fruit from the database.
     *
     * @param fruitName name of the fruit
     * @return matching fruit information
     */
    @Query("SELECT * FROM fruit_info_table WHERE fruit_name = :fruitName")
    suspend fun getFruitByName(fruitName: String): Fruit

    /**
     * Get trees filtered by fruit type from the database.
     *
     * @param fruit name of the fruit
     * @return trees of the specified fruit type
     */
    @Query("SELECT tree.* FROM tree_info_table tree INNER JOIN fruit_info_table ON fruit_type = fruitId WHERE fruit_name = :fruit")
    suspend fun filterByFruit(fruit: String): List<Tree>

    /**
     * Get the trees fruiting in a given month from the database.
     * The months are represented by a single letter, beginning with A representing January,
     * and ending with L representing December.
     *
     * @param month letter representing the month
     * @return all trees for the specified month
     */
    @Query("SELECT tree.* FROM tree_info_table tree INNER JOIN fruit_info_table ON fruit_type = fruitId WHERE fruit_season LIKE '%'+:month+'%'")
    suspend fun getInSeasonTrees(month: String): List<Tree>

    /**
     * Get all fruit names from the database.
     *
     * @return all fruit names
     */
    @Query("SELECT fruit_name FROM fruit_info_table")
    suspend fun getFruitNamesList(): Array<String>
}