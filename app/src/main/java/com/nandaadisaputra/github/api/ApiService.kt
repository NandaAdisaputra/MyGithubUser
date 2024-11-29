package com.nandaadisaputra.github.api
import com.nandaadisaputra.github.data.room.user.detail.DetailUserEntity
import com.nandaadisaputra.github.data.room.user.UserResponse
import com.nandaadisaputra.github.data.room.user.UsersEntity
import retrofit2.Call
import retrofit2.http.*

interface ApiService { // Interface Retrofit untuk mendefinisikan metode request ke API GitHub

    @GET("search/users") // Endpoint untuk mencari pengguna berdasarkan query
    @Headers("Authorization: token put your token here") // Header untuk autentikasi dengan token
    fun getSearchUsers(
        @Query(value = "q") query: String // Parameter pencarian untuk query pengguna
    ): Call<UserResponse> // Mengembalikan response dalam bentuk UserResponse

    @GET("users/{username}") // Endpoint untuk mendapatkan detail pengguna berdasarkan username
    @Headers("Authorization: token put your token here") // Header untuk autentikasi dengan token
    fun getUserDetail(
        @Path("username") username: String // Menyediakan username pengguna untuk mendapatkan detailnya
    ): Call<DetailUserEntity> // Mengembalikan response dalam bentuk DetailUserEntity

    @GET("users/{username}/followers") // Endpoint untuk mendapatkan daftar followers pengguna
    @Headers("Authorization: token put your token here") // Header untuk autentikasi dengan token
    fun getFollowers(
        @Path("username") username: String // Menyediakan username pengguna untuk mendapatkan daftar followers
    ): Call<ArrayList<UsersEntity>> // Mengembalikan daftar followers dalam bentuk ArrayList<UsersEntity>

    @GET("users/{username}/following") // Endpoint untuk mendapatkan daftar following pengguna
    @Headers("Authorization: token put your token here") // Header untuk autentikasi dengan token
    fun getFollowing(
        @Path("username") username: String // Menyediakan username pengguna untuk mendapatkan daftar following
    ): Call<ArrayList<UsersEntity>> // Mengembalikan daftar following dalam bentuk ArrayList<UsersEntity>
}
