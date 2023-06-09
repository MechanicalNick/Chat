package com.tinkoff.homework.utils

object Const {
    const val DELAY: Long = 5L
    const val DB_NAME = "roomdb"
    const val MAX_MESSAGE_COUNT = 5000L
    const val MAX_MESSAGE_COUNT_IN_DB = 50L
    const val MAX_MESSAGE_ON_PAGE = 20L
    const val MESSAGE_THRESHOLD = 5L
    const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"
    const val IMAGE_PREFIX = "[image]"
    // [my dog](/user_uploads/54137/TFFOPnsTF2C9Z1t2MfBwLh66/image.jpg)
    // Паттерн: []()
    const val IMAGE_PATTERN = "\\[(.*?)\\](\\((.*?)\\))"
    const val SITE = "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"
    const val SHORT_SITE = "https://tinkoff-android-spring-2023.zulipchat.com"
    const val EMAIL = "pozdnyakovnp@gmail.com"
    const val API_KEY = "3RbTG10x4EqJcoa4ODNfp07CmDBccUKB"
}