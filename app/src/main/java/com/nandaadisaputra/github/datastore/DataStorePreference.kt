package com.nandaadisaputra.github.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.nandaadisaputra.github.data.constant.Const.Constants.DARK_MODE_KEY
import com.nandaadisaputra.github.data.constant.Const.Constants.THEME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStorePreference(context: Context) {
    // Membuat DataStore untuk menyimpan preferensi dengan nama 'THEME_KEY'
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = THEME_KEY)
    // Menyimpan kunci untuk preferensi mode gelap (dark mode)
    companion object {
        val darkModeKey = booleanPreferencesKey(DARK_MODE_KEY)
    }

    private val dataStore = context.dataStore  // Mengakses instance DataStore
    // Fungsi untuk mengatur (set) preferensi mode gelap (dark mode)
    suspend fun setTheme(isDarkMode: Boolean) {
        // Menggunakan operasi 'edit' untuk memodifikasi preferensi DataStore
        dataStore.edit { preferences ->
            // Menyimpan nilai mode gelap
            preferences[darkModeKey] = isDarkMode
        }
    }
    // Fungsi untuk mengambil (get) nilai preferensi mode gelap dalam bentuk flow
    fun getTheme(): Flow<Boolean> {
        // Menangani pengecualian apabila terjadi kesalahan saat membaca DataStore
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) { // Jika ada kesalahan IO, mengembalikan preferensi kosong
                    emit(emptyPreferences())
                } else {
                    throw exception // Melempar pengecualian lainnya
                }
            }
            .map { preferences ->
                // Mengambil nilai mode gelap dari preferensi, atau default ke false jika tidak ditemukan
                val uiMode = preferences[darkModeKey] ?: false
                uiMode // Mengembalikan nilai mode gelap
            }
    }

}