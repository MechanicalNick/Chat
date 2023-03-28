package com.tinkoff.homework.data.domain

class Stream(val id: Int,
             val name: String,
             val topics: List<Topic>,
             val isSubscribed: Boolean,
             var isExpanded: Boolean)