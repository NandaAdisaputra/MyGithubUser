package com.nandaadisaputra.github.ui.fragment.followers


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.api.ApiService
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
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
class FollowersViewModel @Inject constructor(
    private val apiService: ApiService, // Menyuntikkan ApiService untuk memanggil API
    private val dataStorePreference: DataStorePreference // Menyuntikkan DataStorePreference untuk mengelola pengaturan tema
) : BaseViewModel() {

    private val _followers = MutableLiveData<ArrayList<UsersEntity>>() // Mendeklarasikan LiveData untuk menyimpan daftar followers
    val followers: MutableLiveData<ArrayList<UsersEntity>> = _followers // Menyediakan LiveData yang bisa diobservasi untuk daftar followers
    val showLoading = MutableLiveData<Boolean>() // Menyediakan LiveData untuk menunjukkan status loading

    // Fungsi untuk memuat daftar followers dari API berdasarkan username
    fun setListFollowers(username: String) {
        showLoading.postValue(true) // Menandakan bahwa data sedang dimuat
        apiService.getFollowers(username) // Memanggil API untuk mendapatkan daftar followers
            .enqueue(object : Callback<ArrayList<UsersEntity>> { // Menangani respon API
                override fun onResponse(
                    call: Call<ArrayList<UsersEntity>>,
                    response: Response<ArrayList<UsersEntity>>
                ) {
                    if (response.isSuccessful) { // Jika respon sukses
                        followers.postValue(response.body()) // Mengupdate LiveData dengan data followers
                        showLoading.postValue(false) // Menandakan bahwa data telah selesai dimuat
                    }
                }

                override fun onFailure(call: Call<ArrayList<UsersEntity>>, t: Throwable) {
                    Timber.d(t.message!!) // Menangani error dan mencatat pesan kesalahan dengan Timber
                }
            })
    }

    // Fungsi untuk mengakses daftar followers yang dapat diamati oleh UI
    fun getListFollowers(): LiveData<ArrayList<UsersEntity>> {
        return followers // Mengembalikan LiveData followers
    }

    // Mengambil preferensi tema (gelap/terang) dari DataStore
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk menyimpan preferensi tema (gelap/terang)
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode) // Menyimpan preferensi tema ke DataStore
        }
    }
}
