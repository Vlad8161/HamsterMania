package com.unrealmojo.hamstermania

import android.arch.persistence.room.Room
import android.content.Context
import com.unrealmojo.hamstermania.core.BASE_URL
import com.unrealmojo.hamstermania.core.HamsterDatabase
import com.unrealmojo.hamstermania.core.HamsterStorage
import com.unrealmojo.hamstermania.core.ServerApi
import com.unrealmojo.hamstermania.model.HamsterModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideHamsterModel(serverApi: ServerApi, hamsterStorage: HamsterStorage): HamsterModel =
            HamsterModel(context, serverApi, hamsterStorage)

    @Provides
    @Singleton
    fun provideHamsterStorage(hamsterDatabase: HamsterDatabase): HamsterStorage =
            HamsterStorage(hamsterDatabase)

    @Provides
    @Singleton
    fun provideHamsterDatabase(): HamsterDatabase = Room
            .databaseBuilder(context, HamsterDatabase::class.java, "hamsters.db")
            .build()

    @Provides
    @Singleton
    fun provideServerApi(): ServerApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ServerApi::class.java)
}
