package com.nandaadisaputra.github.ui.fragment.following

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
class FollowingViewModel @Inject constructor(
    private val apiService: ApiService, // Menggunakan ApiService untuk mengakses data API
    private val dataStorePreference: DataStorePreference // Menggunakan DataStorePreference untuk menyimpan preferensi tema
) : BaseViewModel() {

    val showLoading = MutableLiveData<Boolean>() // Menyimpan status loading untuk menampilkan spinner atau indikator pemuatan
    private val _following = MutableLiveData<ArrayList<UsersEntity>>() // Menyimpan daftar pengguna yang di-follow
    val following: MutableLiveData<ArrayList<UsersEntity>> = _following // Publikasi data followers untuk observasi

    // Fungsi untuk mengambil daftar following berdasarkan username
    fun setListFollowing(username: String) {
        showLoading.postValue(true) // Menampilkan indikator loading
        apiService.getFollowing(username) // Mengambil data following dari API menggunakan username
            .enqueue(object : Callback<ArrayList<UsersEntity>> {
                // Fungsi ketika respons API berhasil diterima
                override fun onResponse(
                    call: Call<ArrayList<UsersEntity>>,
                    response: Response<ArrayList<UsersEntity>>
                ) {
                    if (response.isSuccessful) {
                        following.postValue(response.body()) // Menyimpan data following ke LiveData
                        showLoading.postValue(false) // Menyembunyikan indikator loading setelah respons diterima
                    }
                }

                // Fungsi ketika terjadi kegagalan dalam memanggil API
                override fun onFailure(call: Call<ArrayList<UsersEntity>>, t: Throwable) {
                    Timber.d(t.message!!) // Menampilkan log error jika API gagal
                }
            })
    }

    // Fungsi untuk mendapatkan daftar following yang disimpan di LiveData
    fun getListFollowing(): LiveData<ArrayList<UsersEntity>> {
        return following // Mengembalikan LiveData yang berisi daftar following
    }

    // Mengambil preferensi tema dari DataStorePreference dan mengonversinya menjadi LiveData
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Fungsi untuk mengubah tema (dark mode atau light mode)
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode) // Menyimpan preferensi tema ke DataStore
        }
    }
}
