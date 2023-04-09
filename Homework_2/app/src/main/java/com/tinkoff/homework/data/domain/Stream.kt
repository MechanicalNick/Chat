package com.tinkoff.homework.data.domain

class Stream(
    val id: Long,
    val name: String,
    val topics: MutableList<Topic>,
    var isExpanded: Boolean
)