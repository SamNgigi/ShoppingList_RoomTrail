package com.hai.shoppinglist_roomtrail.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hai.shoppinglist_roomtrail.Graph
import com.hai.shoppinglist_roomtrail.data.room.ItemsWithListAndStore
import com.hai.shoppinglist_roomtrail.data.room.model.Item
import com.hai.shoppinglist_roomtrail.domain.repository.Repository
import com.hai.shoppinglist_roomtrail.ui.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


data class HomeState(
    val items: List<ItemsWithListAndStore> = emptyList(),
    val category: Category = Category(),
    val itemChecked: Boolean = false
)

class HomeViewModel(
    private val repository: Repository = Graph.repository
): ViewModel(){
    var state by mutableStateOf(HomeState())
        private set
    init {
        getItems()
    }

    private fun getItems(){
        viewModelScope.launch {
            repository.itemsWithListAndStore.collectLatest {
                state = state.copy(
                    items = it
                )
            }
        }
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    private fun filterBy(shoppingListId: Int){
        if(shoppingListId != 10001 ){
            viewModelScope.launch {
                repository.getItemsByListIdWithListAndStore(shoppingListId).collectLatest {
                    state = state.copy(items = it)
                }
            }
        } else {
            getItems()
        }
    }

    fun onChangeCategory(category: Category){
        state = state.copy(category = category)
        filterBy(category.id) // shoppingListId
    }

    fun onItemCheckedChange(item: Item, isChecked: Boolean){
        viewModelScope.launch {
            repository.updateItem(
                item = item.copy(isChecked = isChecked)
            )
        }
    }






}