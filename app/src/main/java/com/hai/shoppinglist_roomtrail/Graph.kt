package com.hai.shoppinglist_roomtrail

import android.content.Context
import com.hai.shoppinglist_roomtrail.data.room.ShoppingListDB
import com.hai.shoppinglist_roomtrail.domain.repository.Repository

object Graph {
    lateinit var db:ShoppingListDB
        private set

    val repository by lazy {
        Repository(
            shoppingListDao = db.shoppingListDao(),
            itemDao = db.itemDao(),
            storeDao = db.storeDao()

        )
    }

    fun provide(context: Context){
        db = ShoppingListDB.getDatabase(context)
    }
}