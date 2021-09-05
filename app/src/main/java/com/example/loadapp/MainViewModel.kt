package com.example.loadapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _event = MutableLiveData<Event<Item>>()
    val event: LiveData<Event<Item>>
        get() = _event

    lateinit var currentSelectItem: Item

    fun selectItem(item: Item) {
        currentSelectItem = item
    }

    fun download() {
        if (this::currentSelectItem.isInitialized) {
            _event.value = Event(currentSelectItem)
        } else {
            _event.value = Event(Item.NULL)
        }
    }
}