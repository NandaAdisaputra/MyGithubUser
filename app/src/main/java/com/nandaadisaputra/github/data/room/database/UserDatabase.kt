package com.nandaadisaputra.github.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.favorite.FavoriteUsersDao
import com.nandaadisaputra.github.data.room.login.LoginDao
import com.nandaadisaputra.github.data.room.login.LoginEntity


@Database(entities = [
    FavoriteEntity::class,
    LoginEntity::class], version = 9, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteUsersDao
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    Const.Cons.TAG
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}