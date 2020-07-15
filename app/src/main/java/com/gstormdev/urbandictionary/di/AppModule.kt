package com.gstormdev.urbandictionary.di

import android.app.Application
import com.gstormdev.urbandictionary.BuildConfig
import com.gstormdev.urbandictionary.UrbanDictApp
import com.gstormdev.urbandictionary.api.UrbanDictionaryRestClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, DataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providesHttpClient(): OkHttpClient {
        // Automatically add host and api key to each request, so we don't have to specify it in the rest client
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com")
                .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }
        return builder.build()
    }

    @Singleton
    @Inject
    @Provides
    fun provideRestClient(httpClient: OkHttpClient): UrbanDictionaryRestClient {
        return Retrofit.Builder()
            .baseUrl("https://mashape-community-urban-dictionary.p.rapidapi.com/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UrbanDictionaryRestClient::class.java)
    }

    @Provides
    fun providesApplication(app: UrbanDictApp): Application { return app }
}