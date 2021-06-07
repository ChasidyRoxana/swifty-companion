package com.example.companion.data.network

import com.example.companion.data.model.TokenApi
import com.example.companion.data.model.UserApi
import retrofit2.Call
import retrofit2.http.*

interface CompanionApi {

    @GET("v2/users/{login}")
    fun getUserInformation(
        @Header("Authorization") token: String,
        @Path("login") login: String,
    ): Call<UserApi>

    @POST("oauth/token")
    fun getToken(
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Call<TokenApi>
}