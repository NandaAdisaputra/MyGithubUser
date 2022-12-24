package com.nandaadisaputra.github.data.room.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteUsersDao {
    @Insert
    fun insert(favorite: FavoriteEntity)

    @Query("SELECT count(*) FROM favoriteentity WHERE favoriteentity.id = :id")
    fun check(id: Int): Int

    @Query("DELETE FROM favoriteentity WHERE favoriteentity.id = :id")
    fun delete(id: Int): Int

    @Query("SELECT * from favoriteentity")
    fun getAllFavorites(): LiveData<List<FavoriteEntity>>
}