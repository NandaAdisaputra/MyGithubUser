package com.nandaadisaputra.github.data.room.login

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.crocodic.core.data.CoreDao


@Dao
interface LoginDao: CoreDao<LoginEntity> {
    @Query("DELETE FROM LoginEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM LoginEntity WHERE idRoom = 1")
    fun getLogin(): LiveData<LoginEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM LoginEntity WHERE idRoom = 1)")
    suspend fun isLogin(): Boolean
}