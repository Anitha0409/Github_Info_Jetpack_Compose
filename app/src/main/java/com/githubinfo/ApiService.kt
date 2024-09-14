package com.githubinfo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

var retrofit = Retrofit.Builder()
               .baseUrl("https://api.github.com/")
               .addConverterFactory(GsonConverterFactory.create()).build()

var userService = retrofit.create(UserService::class.java)


interface UserService{
    @GET("/users/{username}")
    @Headers("Authorization: token ")
    suspend fun getUserDetails(@Path ("username") username: String): UserData

    @GET("/users/{username}/repos")
    @Headers("Authorization: token ")
    suspend fun getUserRepoDetails(@Path("username") username: String): List<RepoData>

    @GET("/repos/{owner}/{repo}")
    @Headers("Authorization: token ")
    suspend fun getRepoDetails(@Path("owner") owner:String,
                               @Path("repo") repo:String): RepoDetailData
}



