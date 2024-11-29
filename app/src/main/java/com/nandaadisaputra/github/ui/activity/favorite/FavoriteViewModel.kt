package com.nandaadisaputra.github.ui.activity.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.datastore.DataStorePreference
import com.nandaadisaputra.github.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    application: Application,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {

    // Menginisialisasi repository untuk mengakses data favorit
    private val mFavoriteRepository: UserRepository? = UserRepository(application)

    // Fungsi untuk mengambil semua data favorit
    fun getAllFavorites(): LiveData<List<FavoriteEntity>>? = mFavoriteRepository?.getAllFavorites()

    // LiveData untuk mendapatkan status tema (gelap/terang) dari DataStore
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk mengatur tema (gelap/terang) dan menyimpannya ke DataStore
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode) // Menyimpan preferensi tema ke DataStore
        }
    }
}
