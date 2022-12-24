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
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
): BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    val listFollowers = MutableLiveData<ArrayList<UsersEntity>>()

    fun setListFollowers(username: String) {
        showLoading.postValue(true)
        apiService.getFollowers(username)
            .enqueue(object : Callback<ArrayList<UsersEntity>>{
                override fun onResponse(
                    call: Call<ArrayList<UsersEntity>>,
                    response: Response<ArrayList<UsersEntity>>
                ) {
                    if (response.isSuccessful){
                        listFollowers.postValue(response.body())
                        showLoading.postValue(false)
                    }
                }

                override fun onFailure(call: Call<ArrayList<UsersEntity>>, t: Throwable) {
                    Timber.d(t.message!!)
                }

            })
    }

    fun getListFollowers() : LiveData<ArrayList<UsersEntity>>{
        return listFollowers
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}