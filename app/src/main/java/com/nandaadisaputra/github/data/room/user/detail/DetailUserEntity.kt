package com.nandaadisaputra.github.data.room.user.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class DetailUserEntity(
    @PrimaryKey
    @Expose
    @SerializedName("id_room")
    val idRoom: Int,
    @Expose
    @SerializedName("login")
    val login: String,
    @Expose
    @SerializedName("avatar_url")
    val image: String,
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("public_repos")
    val public_repos: Int,
    @Expose
    @SerializedName("bio")
    val bio: String,
    @Expose
    @SerializedName("followers")
    val followers: Int,
    @Expose
    @SerializedName("following")
    val following: Int,
    @Expose
    @SerializedName("location")
    val location: String,
)