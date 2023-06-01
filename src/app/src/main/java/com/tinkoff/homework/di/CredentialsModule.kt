package com.tinkoff.homework.di

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.CredentialsImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CredentialsModule {
    @Binds
    @Singleton
    fun bindsCredentials(impl: CredentialsImpl): Credentials

    @Binds
    @Singleton
    fun bindApiUrlProvider(impl: ApiUrlProviderImpl): ApiUrlProvider
}