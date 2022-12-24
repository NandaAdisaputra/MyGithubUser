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