package com.nandaadisaputra.github.data.room.user.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detailuserentity")
data class DetailUserEntity(

    // ID entitas yang merupakan primary key untuk DetailUserEntity
    @PrimaryKey
    @Expose
    @SerializedName("id_room")
    val idRoom: Int,

    // Username pengguna (login)
    @Expose
    @SerializedName("login")
    val login: String,

    // URL avatar gambar pengguna
    @Expose
    @SerializedName("avatar_url")
    val image: String,

    // ID pengguna yang unik
    @Expose
    @SerializedName("id")
    val id: String,

    // Nama lengkap pengguna
    @Expose
    @SerializedName("name")
    val name: String,

    // Jumlah repositori publik pengguna di GitHub
    @Expose
    @SerializedName("public_repos")
    val public_repos: Int,

    // Deskripsi singkat pengguna (bio)
    @Expose
    @SerializedName("bio")
    val bio: String,

    // Jumlah pengikut pengguna
    @Expose
    @SerializedName("followers")
    val followers: Int,

    // Jumlah pengguna yang diikuti
    @Expose
    @SerializedName("following")
    val following: Int,

    // Lokasi pengguna
    @Expose
    @SerializedName("location")
    val location: String,
)
