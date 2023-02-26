package com.mockknights.petshelter.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mockknights.petshelter.data.remote.PetShelterAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("NAME", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideMoshi(): Moshi {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        return moshi
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        return httpLoggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
                    .header("ApiKey", "mWIwALVZo3a0evMfbUgkl/gLvRis1/w99To0AamBN+0=")
                    .build()
                chain.proceed(newRequest)
            }
            .authenticator { _, response ->
                if(response.request.url.encodedPath.contains("api/auth/signin")) {
                    response.request.newBuilder()
                        .header("Authorization", "${sharedPreferences.getString("CREDENTIAL", null)}")
                        .build()
                }
                else if(response.request.url.encodedPath.contains("api/upload")) {
                    val response = response.request.newBuilder()
                        .header("Authorization", "Bearer ${sharedPreferences.getString("TOKEN", null)}")
                        .build()
                    Log.d("RESPONSE AUTH", "${response.header("Authorization")}")
                    response
                }
                else {
                    response.request.newBuilder()
                        .build()
                }
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()
        return okHttpClient
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit): PetShelterAPI {
        var api: PetShelterAPI = retrofit.create(PetShelterAPI::class.java)
        return api
    }



}