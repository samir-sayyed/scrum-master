package com.example.dailyscrum.data

data class ScrumItem(val id : Int,
                     val title:String,
                     val duration: Int,
                     val attendeeList : List<Attendee>,
                     val theme:String)
