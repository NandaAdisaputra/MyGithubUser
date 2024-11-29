package com.nandaadisaputra.github.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.favorite.FavoriteUsersDao

// UserRepository bertugas untuk berinteraksi dengan database dan menyajikan data ke ViewModel
class UserRepository(application: Application) {
    // Mendeklarasikan objek FavoriteUsersDao yang digunakan untuk operasi data favorit
    private val mFavoriteDao: FavoriteUsersDao
    // Inisialisasi repository dengan mengakses UserDatabase dan mengambil dao untuk mengelola data favorit
    init {
        // Mendapatkan instance UserDatabase dengan menggunakan Application context
        val db = UserDatabase.getDatabase(application)
        // Mendapatkan FavoriteUsersDao dari database untuk mengakses tabel favorit
        mFavoriteDao = db.favoriteDao()
    }
    // Fungsi untuk mengambil semua data favorit yang ada di database
    // Mengembalikan LiveData yang berisi list FavoriteEntity
    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getAllFavorites()
    // Fungsi untuk mengecek apakah suatu entri favorit sudah ada berdasarkan ID
    // Menggunakan mFavoriteDao untuk memanggil fungsi check pada Dao
    fun check(id: Int) = mFavoriteDao.check(id)
    // Fungsi untuk menyisipkan entri favorit baru ke dalam database
    // Menggunakan mFavoriteDao untuk memanggil fungsi insert pada Dao
    fun insert(favorite: FavoriteEntity) = mFavoriteDao.insert(favorite)
    // Fungsi untuk menghapus entri favorit berdasarkan ID
    // Menggunakan mFavoriteDao untuk memanggil fungsi delete pada Dao
    fun delete(id: Int) = mFavoriteDao.delete(id)

}