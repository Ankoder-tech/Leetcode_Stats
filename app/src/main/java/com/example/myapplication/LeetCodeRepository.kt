package com.example.myapplication

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class LeetCodeRepository {
    private val api = RetrofitInstance.api
    suspend fun getProfile(
        username: String
    ): JsonObject {
        val response = api.getProfile(username)
        if (!response.isSuccessful) {
            throw Exception("User not found or API error")
        }
        return response.body()
            ?: throw Exception("Empty profile response")
    }
    suspend fun getBadges(
        username: String
    ): JsonObject {
        val response = api.getBadges(username)
        if (!response.isSuccessful) {
            throw Exception("Unable to fetch badges")
        }
        return response.body()
            ?: throw Exception("Empty badges response")
    }
    suspend fun getSolved(
        username: String
    ): JsonObject {
        val response = api.getSolved(username)
        if (!response.isSuccessful) {
            throw Exception("Unable to fetch solved questions")
        }
        return response.body() ?: throw Exception("Empty solved response")
    }
    suspend fun getSubmissions(
        username: String,
        limit: Int = 20
    ): JsonElement {
        val response = api.getSubmissions(username, limit)
        if (!response.isSuccessful) {
            throw Exception("Unable to fetch submissions")
        }
        return response.body()
            ?: throw Exception("Empty submissions response")
    }

}