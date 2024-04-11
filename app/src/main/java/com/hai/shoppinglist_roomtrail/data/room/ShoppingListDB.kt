package com.hai.shoppinglist_roomtrail.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hai.shoppinglist_roomtrail.data.room.converters.DateConverter
import com.hai.shoppinglist_roomtrail.data.room.model.Item
import com.hai.shoppinglist_roomtrail.data.room.model.ShoppingList
import com.hai.shoppinglist_roomtrail.data.room.model.Store

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [ShoppingList::class, Item::class, Store::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingListDB: RoomDatabase() {
    abstract fun shoppingListDao():ShoppingListDao
    abstract fun itemDao(): ItemDao
    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile
        var INSTANCE: ShoppingListDB? = null
        fun getDatabase(context: Context): ShoppingListDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    ShoppingListDB::class.java,
                    "shoppingList_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}