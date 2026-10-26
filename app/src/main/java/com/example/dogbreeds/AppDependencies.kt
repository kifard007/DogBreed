package com.example.dogbreeds

import android.content.Context
import androidx.room.Room
import com.example.dogbreeds.data.BreedRepository
import com.example.dogbreeds.data.DogApiService
import com.example.dogbreeds.data.RemoteDataSource
import com.example.dogbreeds.data.local.BreedDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object AppDependencies {

    lateinit var repository: BreedRepository
        private set

    fun init(context: Context) {
        if (::repository.isInitialized) return

        val database = Room.databaseBuilder(
            context.applicationContext,
            BreedDatabase::class.java,
            "dog_breeds.db"
        ).build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttp = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { // ← добавьте этот блок
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dogapi.dog/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttp)
            .build()

        val api = retrofit.create(DogApiService::class.java)
        val remote = RemoteDataSource(api)
        repository = BreedRepository(remote, database.breedDao())
    }
}