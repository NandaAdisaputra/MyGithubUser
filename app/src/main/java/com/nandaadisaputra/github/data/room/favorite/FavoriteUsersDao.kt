package com.nandaadisaputra.github.data.room.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteUsersDao {

    // Fungsi untuk menyimpan entitas Favorite ke dalam database
    @Insert
    fun insert(favorite: FavoriteEntity)

    // Fungsi untuk memeriksa apakah entitas dengan ID tertentu sudah ada di database
    @Query("SELECT count(*) FROM favoriteentity WHERE favoriteentity.id = :id")
    fun check(id: Int): Int

    // Fungsi untuk menghapus entitas dengan ID tertentu dari database
    @Query("DELETE FROM favoriteentity WHERE favoriteentity.id = :id")
    fun delete(id: Int): Int

    // Fungsi untuk mendapatkan semua entitas favorit dalam bentuk LiveData
    @Query("SELECT * from favoriteentity")
    fun getAllFavorites(): LiveData<List<FavoriteEntity>>
}
