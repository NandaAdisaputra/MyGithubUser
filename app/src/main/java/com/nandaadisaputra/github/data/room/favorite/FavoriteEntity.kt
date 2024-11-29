package com.nandaadisaputra.github.data.room.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteentity")
data class FavoriteEntity(

    // ID entitas yang akan menjadi primary key dan otomatis di-generate oleh Room
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    // URL avatar pengguna yang disimpan dalam tabel favorit
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    // Nama pengguna (username) yang disimpan dalam tabel favorit
    @ColumnInfo(name = "username")
    var username: String? = null
)
