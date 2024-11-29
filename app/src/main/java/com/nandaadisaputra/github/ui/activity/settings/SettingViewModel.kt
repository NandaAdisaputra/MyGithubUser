package com.nandaadisaputra.github.ui.activity.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {

    // LiveData untuk mengamati status tema (gelap/terang) yang disimpan di DataStore
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk menyimpan preferensi tema ke DataStore
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // Menyimpan preferensi tema ke DataStore
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}
