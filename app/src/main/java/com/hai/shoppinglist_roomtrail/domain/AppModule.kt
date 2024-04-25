package com.hai.shoppinglist_roomtrail.domain

import android.content.Context
import androidx.room.Room
import com.hai.shoppinglist_roomtrail.data.room.ItemDao
import com.hai.shoppinglist_roomtrail.data.room.ShoppingListDB
import com.hai.shoppinglist_roomtrail.data.room.ShoppingListDao
import com.hai.shoppinglist_roomtrail.data.room.StoreDao
import com.hai.shoppinglist_roomtrail.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesShoppingListDB(@ApplicationContext appContext: Context): ShoppingListDB {
        return Room.databaseBuilder(
            appContext,
            ShoppingListDB::class.java,
            "shoppingList_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesShoppingListDao(appDB: ShoppingListDB): ShoppingListDao{
        return appDB.shoppingListDao()
    }

    @Provides
    @Singleton
    fun providesStoreDao(appDB: ShoppingListDB): StoreDao{
        return appDB.storeDao()
    }

    @Provides
    @Singleton
    fun providesItemDao(appDB: ShoppingListDB): ItemDao{
        return appDB.itemDao()
    }

    @Provides
    @Singleton
    fun providesRepository(
        shoppingListDao: ShoppingListDao,
        storeDao: StoreDao,
        itemDao: ItemDao
    ): Repository{
        return Repository(
            shoppingListDao, storeDao, itemDao
        )
    }


}