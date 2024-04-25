package com.hai.shoppinglist_roomtrail.domain.repository

import com.hai.shoppinglist_roomtrail.data.room.ItemDao
import com.hai.shoppinglist_roomtrail.data.room.ShoppingListDao
import com.hai.shoppinglist_roomtrail.data.room.StoreDao
import com.hai.shoppinglist_roomtrail.data.room.model.Item
import com.hai.shoppinglist_roomtrail.data.room.model.ShoppingList
import com.hai.shoppinglist_roomtrail.data.room.model.Store
import javax.inject.Inject

class Repository @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
    private val storeDao: StoreDao,
    private val itemDao: ItemDao
) {
    val itemsWithListAndStore = shoppingListDao.getItemsWithListAndStore()
    val store = storeDao.getAllStores()

    fun getItemByIdWithListAndStore(itemId: Int) = shoppingListDao.getItemByIdWithListAndStore(itemId)
    fun getItemsByListIdWithListAndStore(listId: Int) = shoppingListDao.getItemsByListIdWithListAndStore(listId)

    suspend fun insertShoppingList(shoppingList: ShoppingList){
        shoppingListDao.insert(shoppingList)
    }

    suspend fun insertStore(store: Store){
        storeDao.insert(store)
    }

    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }

    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }

}