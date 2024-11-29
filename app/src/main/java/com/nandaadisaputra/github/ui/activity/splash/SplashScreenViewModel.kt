package com.nandaadisaputra.github.ui.activity.splash

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor( private val dataStorePreference: DataStorePreference) : BaseViewModel() {
    // Fungsi splash untuk menunggu selama 3 detik
    fun splash(done: (Boolean) -> Unit) = viewModelScope.launch {
        delay(3000) // Delay selama 3 detik
        done(true)  // Panggil callback setelah delay selesai
    }
    // Mengambil pengaturan tema dari DataStore
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk mengubah tema
    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}