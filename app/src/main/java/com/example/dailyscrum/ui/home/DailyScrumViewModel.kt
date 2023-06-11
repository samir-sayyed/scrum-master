package com.example.dailyscrum.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.dailyscrum.data.ScrumItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


class DailyScrumViewModel:ViewModel() {

    private val _scrumItemList = mutableStateListOf<ScrumItem>()
    val scrumItemList: List<ScrumItem>
        get() = _scrumItemList

//    private val _attendeeList = attendeeList().toMutableStateList()
//    val attendeeList: List<Attendee>
//        get() = _attendeeList

    fun addScrumItem(scrumItem:ScrumItem){
        _scrumItemList.add(scrumItem)
    }

    fun editScrumItem(id: Int ,scrumItem:ScrumItem){
        var meeting: ScrumItem? = null
           scrumItemList.forEach { item ->
                if (item.id == id) meeting = item
        }
        _scrumItemList.remove(meeting)
        _scrumItemList.add(scrumItem)
    }



   val counter = flow {
       var value = 0
       while (true){
           emit(value++)
           delay(3000)
       }
   }
}

//private fun getWellnessTasks() = List(10) { i -> ScrumItem("title $i", i+2,i*10) }
//private fun attendeeList() = List(10) { i -> Attendee(i, "attendee$i") }
