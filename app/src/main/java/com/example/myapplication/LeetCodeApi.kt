package com.example.myapplication

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeetCodeApi {
    @GET("{username}")
    suspend fun getProfile(
        @Path("username") username: String
    ): Response<JsonObject>
    @GET("{username}/solved")
    suspend fun getSolved(
        @Path("username") username: String
    ): Response<JsonObject>
    @GET("{username}/badges")
    suspend fun getBadges(
        @Path("username") username: String
    ): Response<JsonObject>
    @GET("{username}/submission")
    suspend fun getSubmissions(
        @Path("username") username: String,
        @Query("limit") limit: Int = 20
    ): Response<JsonElement>
}
object RetrofitInstance {
    private const val BASE_URL = "https://alfa-leetcode-api.onrender.com/"
    val api: LeetCodeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeetCodeApi::class.java)
    }
}