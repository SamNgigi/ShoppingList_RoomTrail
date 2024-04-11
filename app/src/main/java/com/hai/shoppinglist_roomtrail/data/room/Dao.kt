package com.hai.shoppinglist_roomtrail.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hai.shoppinglist_roomtrail.data.room.model.Item
import com.hai.shoppinglist_roomtrail.data.room.model.ShoppingList
import com.hai.shoppinglist_roomtrail.data.room.model.Store
import kotlinx.coroutines.flow.Flow


data class ItemsWithListAndStore(
    @Embedded val item: Item,
    @Embedded val shoppingList: ShoppingList,
    @Embedded val store: Store
)

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE item_id =:itemId")
    fun getItem(itemId: Int): Flow<Item>
}

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(store: Store)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(store: Store)

    @Delete
    suspend fun delete(store: Store)

    @Query("SELECT * FROM stores")
    fun getAllStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE store_id =:storeId")
    fun getStore(storeId: Int): Flow<Store>

}

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingList: ShoppingList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(shoppingList : ShoppingList)

    @Query(
        """
        SELECT * FROM items AS i 
            INNER JOIN shopping_list AS sl ON i.iListIdFk = sl.list_id 
            INNER JOIN stores AS st ON i.storeIdFK = st.store_id
    """
    )
    fun getItemsWithListAndStore(): Flow<List<ItemsWithListAndStore>>

    @Query(
        """
        SELECT * FROM items AS i
            INNER JOIN shopping_list AS sl ON i.iListIdFk = sl.list_id
            INNER JOIN stores AS st ON i.storeIdFK = st.store_id
        WHERE sl.list_id =:listId
    """
    )
    fun getItemsByListIdWithListAndStore(listId: Int): Flow<List<ItemsWithListAndStore>>

    @Query(
        """
        SELECT * FROM items AS i
            INNER JOIN shopping_list AS sl ON i.iListIdFk = sl.list_id
            INNER JOIN stores AS st ON i.storeIdFK = st.store_id
        WHERE i.item_id =:itemId
    """
    )
    fun getItemByIdWithListAndStore(itemId: Int): Flow<ItemsWithListAndStore>

}









