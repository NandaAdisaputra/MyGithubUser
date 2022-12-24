package com.nandaadisaputra.github.api
import com.nandaadisaputra.github.data.room.user.UserResponse
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.data.room.user.detail.DetailUserEntity
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query(value = "q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserEntity>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UsersEntity>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UsersEntity>>
}