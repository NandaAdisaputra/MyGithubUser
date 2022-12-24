package com.nandaadisaputra.github.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.favorite.FavoriteUsersDao

class UserRepository(application: Application) {

    private val mFavoriteDao: FavoriteUsersDao

    init {
        val db = UserDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getAllFavorites()

    fun check(id: Int) = mFavoriteDao.check(id)

    fun insert(favorite: FavoriteEntity) = mFavoriteDao.insert(favorite)

    fun delete(id: Int) = mFavoriteDao.delete(id)

}