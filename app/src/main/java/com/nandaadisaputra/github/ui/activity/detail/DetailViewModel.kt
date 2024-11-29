package com.nandaadisaputra.github.ui.activity.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.github.api.ApiService
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.favorite.FavoriteUsersDao
import com.nandaadisaputra.github.data.room.user.detail.DetailUserEntity
import com.nandaadisaputra.github.datastore.DataStorePreference
import com.nandaadisaputra.github.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    application: Application, // Menyuntikkan objek Application untuk akses ke resources aplikasi
    private val apiService: ApiService, // Menggunakan ApiService untuk memanggil API
    private val dataStorePreference: DataStorePreference // Menggunakan DataStore untuk menyimpan preferensi pengguna
) : BaseViewModel() {

    // LiveData untuk menunjukkan status loading
    val showLoading = MutableLiveData<Boolean>()

    // Variabel untuk mengakses DAO dari database favorit
    private var favoriteUsersDAO: FavoriteUsersDao?

    // Repository untuk mengakses data pengguna
    private val mFavoriteRepository: UserRepository = UserRepository(application)

    // Mengakses database untuk operasi terkait favorit
    private var favoriteUserDatabase: UserDatabase? = UserDatabase.getDatabase(application)

    // Inisialisasi DAO dan database favorit
    init {
        favoriteUsersDAO = favoriteUserDatabase?.favoriteDao()
    }

    // LiveData untuk menyimpan data detail pengguna
    val user = MutableLiveData<DetailUserEntity>()

    // Fungsi untuk mengambil detail pengguna berdasarkan username
    fun setUserDetail(username: String) {
        showLoading.postValue(true) // Menampilkan loading sebelum mengambil data
        apiService.getUserDetail(username)
            .enqueue(object : Callback<DetailUserEntity> {
                override fun onResponse(
                    call: Call<DetailUserEntity>,
                    response: Response<DetailUserEntity>
                ) {
                    // Jika respon API berhasil, simpan data ke LiveData
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                        showLoading.postValue(false) // Sembunyikan loading setelah data diterima
                    }
                }

                override fun onFailure(call: Call<DetailUserEntity>, t: Throwable) {
                    // Menangani kegagalan API, misalnya koneksi jaringan error
                    Timber.d(t.message!!) // Log error
                }
            })
    }

    // Mengembalikan data detail pengguna dalam bentuk LiveData
    fun getUserDetail(): LiveData<DetailUserEntity> {
        return user
    }

    // Mengecek apakah pengguna sudah ada di daftar favorit berdasarkan ID
    fun checkUser(id: Int) = mFavoriteRepository.check(id)

    // Menambahkan pengguna ke daftar favorit
    fun addToFavorite(username: String, id: Int, avatarUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            // Menyimpan data favorit ke database
            val user = FavoriteEntity(
                id,
                avatarUrl,
                username
            )
            mFavoriteRepository.insert(user) // Menyimpan data ke database
        }
    }

    // Menghapus pengguna dari daftar favorit
    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteRepository.delete(id) // Menghapus data dari database
        }
    }

    // Mengambil pengaturan tema (gelap/terang) dari DataStore
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    // Menyimpan pengaturan tema (gelap/terang) ke DataStore
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode) // Menyimpan tema pilihan pengguna
        }
    }
}
