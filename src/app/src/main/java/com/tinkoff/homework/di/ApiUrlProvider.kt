package com.tinkoff.homework.di

import com.tinkoff.homework.utils.Const
import javax.inject.Inject

interface ApiUrlProvider {
    var url: String
}

class ApiUrlProviderImpl @Inject constructor(): ApiUrlProvider {
    override var url: String = Const.SITE
}