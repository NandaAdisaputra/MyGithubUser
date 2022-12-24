package com.nandaadisaputra.github.data.room.login

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class LoginEntity (
    @PrimaryKey
    @Expose
    @SerializedName("id_room")
    val idRoom: Int,
    @Expose
    @SerializedName("userId")
    val userId: String?,
    @Expose
    @SerializedName("name")
    val name:String?,
    @Expose
    @SerializedName("email")
    val email: String?,
    @Expose
    @SerializedName("token")
    val token: String?,
) : Parcelable
