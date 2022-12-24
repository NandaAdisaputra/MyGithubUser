package com.nandaadisaputra.github.ui.activity.login

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import com.nandaadisaputra.github.api.ApiServiceTwo
import com.nandaadisaputra.github.base.viewmodel.BaseViewModel
import com.nandaadisaputra.github.data.room.login.LoginDao
import com.nandaadisaputra.github.data.room.login.LoginEntity
import com.nandaadisaputra.github.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiServiceTwo:ApiServiceTwo,
    private val gson: Gson,
    private val loginDao: LoginDao,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    fun login(email: String, password: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            { apiServiceTwo.login(email, password) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val status = response.getBoolean("error")
                    if (!status) {
                        val data = response.getJSONObject("loginResult").toObject<LoginEntity>(gson)
                        saveUser(data)
                        _apiResponse.send(ApiResponse().responseSuccess())
                    } else {
                        val message = response.getString(ApiCode.MESSAGE)
                        _apiResponse.send(ApiResponse(status = ApiStatus.ERROR, message = message))
                    }
                }
            })
    }
    private fun saveUser(user: LoginEntity) = viewModelScope.launch {
        loginDao.insert(user.copy(idRoom = 1))
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}
