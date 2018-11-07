package com.unrealmojo.hamstermania.core

import com.unrealmojo.hamstermania.data.network.HamsterNetwork
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

val BASE_URL = "http://unrealmojo.com/"

interface ServerApi {
    @GET("/porn/test3")
    fun getHamsters(
            @Header("X-Homo-Client-OS") clientOs: String,
            @Header("X-Homo-Client-Version") clientVersion: String,
            @Header("X-Homo-Client-Model") clientModel: String
    ): Single<List<HamsterNetwork>>
}

