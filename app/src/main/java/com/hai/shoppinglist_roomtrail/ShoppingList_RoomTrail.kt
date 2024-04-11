package com.hai.shoppinglist_roomtrail

import android.app.Application

class ShoppingList_RoomTrail: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}