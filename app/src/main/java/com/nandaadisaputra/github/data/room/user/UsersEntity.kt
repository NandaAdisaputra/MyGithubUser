package com.nandaadisaputra.github.data.room.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UsersEntity(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    val id: Int?,
    @Expose
    @SerializedName("login")
    val login: String?,
    @Expose
    @SerializedName("avatar_url")
    val avatar: String?,
) : Parcelable
