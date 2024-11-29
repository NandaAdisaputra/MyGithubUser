package com.nandaadisaputra.github.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.favorite.FavoriteUsersDao

@Database(entities = [
    FavoriteEntity::class], version = 11, exportSchema = false)  // Menandai kelas ini sebagai database dengan entitas FavoriteEntity
abstract class UserDatabase : RoomDatabase() { // UserDatabase adalah kelas RoomDatabase yang mengatur akses ke data

    abstract fun favoriteDao(): FavoriteUsersDao // Mendeklarasikan DAO untuk FavoriteEntity

    companion object { // Companion object digunakan untuk membuat instance database secara singleton
        @Volatile
        private var INSTANCE: UserDatabase? = null // Properti instance untuk menyimpan referensi database

        fun getDatabase(context: Context): UserDatabase {  // Fungsi untuk mendapatkan instance UserDatabase
            return INSTANCE ?: synchronized(this) { // Jika INSTANCE belum ada, buat instance baru dengan Room
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Menggunakan context aplikasi untuk menghindari kebocoran memori
                    UserDatabase::class.java,// Menentukan kelas database
                    Const.Cons.TAG // Nama database
                )
                    .fallbackToDestructiveMigration() // Menangani perubahan skema yang tidak dipetakan
                    .build() // Membangun database
                INSTANCE = instance // Menyimpan instance untuk penggunaan selanjutnya
                instance
            }
        }
    }
}