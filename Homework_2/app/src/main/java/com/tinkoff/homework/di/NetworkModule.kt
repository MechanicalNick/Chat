package com.tinkoff.homework.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.ZulipChatApi
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesLoggingInterceptorInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
    }

    @Singleton
    @Provides
    fun provideBasicCredentials(): String = Credentials.basic(Const.EMAIL, Const.API_KEY)

    @Singleton
    @Provides
    fun provideAuthInterceptor(basicCredentials: String): Interceptor = Interceptor {
        val newRequest =
            it.request().newBuilder()
                .addHeader("Authorization", basicCredentials)
                .build()
        it.proceed(newRequest)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(authorizationInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideZulipChat(client: OkHttpClient): ZulipChatApi {
        var retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .baseUrl(Const.SITE)
            .build()
        return retrofit.create(ZulipChatApi::class.java)
    }
}