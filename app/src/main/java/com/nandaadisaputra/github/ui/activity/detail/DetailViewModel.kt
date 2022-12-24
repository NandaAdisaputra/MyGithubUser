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
    application: Application,
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    private var favoriteUsersDAO: FavoriteUsersDao?
    private val mFavoriteRepository: UserRepository = UserRepository(application)
    private var favoriteUserDatabase: UserDatabase? =
        UserDatabase.getDatabase(application)
    init {
        favoriteUsersDAO = favoriteUserDatabase?.favoriteDao()
    }

    val user = MutableLiveData<DetailUserEntity>()
    fun setUserDetail(username: String) {
        showLoading.postValue(true)
        apiService.getUserDetail(username)
            .enqueue(object : Callback<DetailUserEntity> {
                override fun onResponse(
                    call: Call<DetailUserEntity>,
                    response: Response<DetailUserEntity>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                        showLoading.postValue(false)
                    }
                }

                override fun onFailure(call: Call<DetailUserEntity>, t: Throwable) {
                    Timber.d(t.message!!)
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserEntity> {
        return user
    }

    fun checkUser(id: Int) = mFavoriteRepository.check(id)

    fun addToFavorite(username: String, id: Int, avatarUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteEntity(
                id,
                avatarUrl,
                username
            )
            mFavoriteRepository.insert(user)
        }
    }

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteRepository.delete(id)
        }
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}
