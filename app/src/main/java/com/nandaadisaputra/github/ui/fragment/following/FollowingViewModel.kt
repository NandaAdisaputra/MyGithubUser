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
class FollowingViewModel @Inject constructor(private val apiService: ApiService, private val dataStorePreference: DataStorePreference): BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    val listFollowing = MutableLiveData<ArrayList<UsersEntity>>()

    fun setListFollowing(username: String) {
        showLoading.postValue(true)
        apiService.getFollowing(username)
            .enqueue(object : Callback<ArrayList<UsersEntity>>{
                override fun onResponse(
                    call: Call<ArrayList<UsersEntity>>,
                    response: Response<ArrayList<UsersEntity>>
                ) {
                    if (response.isSuccessful){
                        listFollowing.postValue(response.body())
                        showLoading.postValue(false)
                    }
                }

                override fun onFailure(call: Call<ArrayList<UsersEntity>>, t: Throwable) {
                    Timber.d(t.message!!)
                }

            })
    }

    fun getListFollowing() : LiveData<ArrayList<UsersEntity>>{
        return listFollowing
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}