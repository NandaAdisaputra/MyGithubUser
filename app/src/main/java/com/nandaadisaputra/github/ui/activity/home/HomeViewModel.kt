package com.nandaadisaputra.github.ui.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.api.ApiService
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.data.room.user.UserResponse
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {

    // LiveData untuk status loading
    val showLoading = MutableLiveData<Boolean>()

    // LiveData untuk daftar pengguna
    val listUsers = MutableLiveData<ArrayList<UsersEntity>>()

    // Fungsi untuk mengirim permintaan pencarian pengguna ke API
    fun setSearchUsers(query: String) {
        showLoading.postValue(true)
        apiService.getSearchUsers(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items) // Menyimpan hasil pencarian
                        showLoading.postValue(false) // Menyembunyikan loading
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Timber.d(t.message!!) // Menangani kegagalan API
                }
            })
    }

    // Mengambil daftar pengguna
    fun getSearchUsers(): LiveData<ArrayList<UsersEntity>> {
        return listUsers
    }

    // LiveData untuk tema (gelap/terang)
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk mengatur tema (gelap/terang)
    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode) // Menyimpan tema yang dipilih
        }
    }
}
