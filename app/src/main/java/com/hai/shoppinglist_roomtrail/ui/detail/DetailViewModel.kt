package com.hai.shoppinglist_roomtrail.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hai.shoppinglist_roomtrail.Graph
import com.hai.shoppinglist_roomtrail.data.room.model.Item
import com.hai.shoppinglist_roomtrail.data.room.model.ShoppingList
import com.hai.shoppinglist_roomtrail.data.room.model.Store
import com.hai.shoppinglist_roomtrail.domain.repository.Repository
import com.hai.shoppinglist_roomtrail.ui.Category
import com.hai.shoppinglist_roomtrail.ui.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class DetailViewModel
    constructor(
        private val itemId: Int,
        private val repository: Repository = Graph.repository
): ViewModel() {
    var state by mutableStateOf(DetailState())
        private set
    init {
        addShoppingList()
        getStores()
        if(itemId != -1){
            viewModelScope.launch {
                repository
                    .getItemByIdWithListAndStore(itemId)
                    .collectLatest {
                        state = state.copy(
                            itemName = it.item.itemName,
                            storeName = it.store.storeName,
                            date = it.item.date,
                            category = Utils.category.find { c->
                                c.id == it.shoppingList.id
                            } ?: Category(),
                            qty =  it.item.qty
                        )
                    }
            }
        }
    }

    init {
        state = if(itemId != -1){
            state.copy(isUpdatingItem = true)
        } else {
            state.copy(isUpdatingItem = false)
        }
    }

    val isFieldsNotEmpty: Boolean
        get() = state.itemName.isNotEmpty() &&
                state.storeName.isNotEmpty() &&
                state.qty.isNotEmpty()

    fun onItemChange(newValue: String){
        state = state.copy(itemName = newValue)
    }
    fun onStoreChange(newValue: String){
        state = state.copy(storeName = newValue)
    }
    fun onQtyChange(newValue: String){
        state = state.copy(qty = newValue)
    }
    fun onDateChange(newValue: Date){
        state = state.copy(date = newValue)
    }
    fun onCategoryChange(newValue: Category){
        state = state.copy(category = newValue)
    }
    fun onScreenDialogDismissed(newValue: Boolean){
        state = state.copy(isScreenDialogDismissed = newValue)
    }

    private fun addShoppingList(){
        viewModelScope.launch {
            Utils.category.forEach{
                repository.insertShoppingList(
                    ShoppingList(id = it.id, name = it.title)
                )
            }
        }
    }
    fun addShoppingItem(){
        viewModelScope.launch {
            repository.insertItem(
                Item(
                    itemName = state.itemName,
                    iListIdFk = state.category.id,
                    date = state.date,
                    qty = state.qty,
                    storeIdFK = state.storeList.find {
                        it.storeName == state.storeName
                    }?.id ?:0,
                    isChecked = false
                )
            )
        }
    }
    fun updateShoppingItem(id: Int){
        viewModelScope.launch {
            repository.insertItem(
                Item(
                    itemName =  state.itemName,
                    iListIdFk = state.category.id,
                    date = state.date,
                    qty = state.qty,
                    storeIdFK = state.storeList.find {
                        it.storeName == state.storeName
                    }?.id ?:0,
                    isChecked = false,
                    id = id
                )
            )
        }
    }

    fun addStore(){
        viewModelScope.launch {
            repository.insertStore(
                Store(storeName = state.storeName, sListIdFk = state.category.id)
            )
        }
    }

    private fun getStores(){
        viewModelScope.launch {
            repository.store.collectLatest {
                state = state.copy(storeList = it)
            }
        }
    }
}



data class DetailState(
    val storeList: List<Store> = emptyList(),
    val itemName: String = "",
    val storeName: String = "",
    val date: Date = Date(),
    val qty: String = "",
    val isScreenDialogDismissed: Boolean = true,
    val isUpdatingItem: Boolean = false,
    val category: Category = Category()
)

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val id: Int): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return DetailViewModel(itemId = id) as T
    }
}