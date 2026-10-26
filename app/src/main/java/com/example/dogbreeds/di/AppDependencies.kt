package com.example.dogbreeds.di

import android.content.Context
import androidx.room.Room
import com.example.dogbreeds.data.local.BreedDatabase
import com.example.dogbreeds.data.remote.DogApiService
import com.example.dogbreeds.data.remote.RemoteDataSource
import com.example.dogbreeds.data.repository.BreedRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {

    lateinit var database: BreedDatabase
        private set
    lateinit var remoteDataSource: RemoteDataSource
        private set
    lateinit var repository: BreedRepository
        private set

    fun init(context: Context) {
        if (::database.isInitialized) return

        database = Room.databaseBuilder(
            context.applicationContext,
            BreedDatabase::class.java,
            "dog_breeds.db"
        ).build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttp)
            .build()

        val api = retrofit.create(DogApiService::class.java)
        remoteDataSource = RemoteDataSource(api)
        repository = BreedRepository(remoteDataSource, database.breedDao())
    }
}