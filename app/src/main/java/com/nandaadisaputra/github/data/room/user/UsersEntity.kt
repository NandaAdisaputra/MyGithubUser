package com.nandaadisaputra.github.data.room.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity  // Menandai kelas ini sebagai entitas Room yang akan dipetakan ke tabel di database SQLite.
@Parcelize  // Menggunakan @Parcelize untuk membuat kelas ini Parcelable, memungkinkan objek untuk dipassing antar komponen Android.
data class UsersEntity(
    @PrimaryKey  // Menandai properti ini sebagai Primary Key dalam tabel database.
    @Expose  // Menandai properti ini untuk disertakan dalam proses serialisasi dan deserialisasi menggunakan Gson.
    @SerializedName("id")  // Menentukan nama field dalam JSON yang dipetakan ke properti ini.
    val id: Int?,  // ID pengguna GitHub, yang bisa null.

    @Expose  // Menandai properti ini untuk disertakan dalam serialisasi.
    @SerializedName("login")  // Nama pengguna GitHub, dipetakan dari field "login" dalam JSON.
    val login: String?,  // Nama login pengguna GitHub, yang bisa null.

    @Expose  // Menandai properti ini untuk disertakan dalam serialisasi.
    @SerializedName("avatar_url")  // URL avatar pengguna GitHub, dipetakan dari field "avatar_url" dalam JSON.
    val avatar: String?,  // URL gambar avatar pengguna GitHub, yang bisa null.
) : Parcelable  // Mengimplementasikan Parcelable agar objek ini dapat dipassing antar Activity/Fragment di Android.
