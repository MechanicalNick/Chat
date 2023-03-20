package com.tinkoff.homework.data

class Stream(val id: Int,
             val name: String,
             val topics: List<Topic>,
             val isSubscribed: Boolean,
             var isExpanded: Boolean)