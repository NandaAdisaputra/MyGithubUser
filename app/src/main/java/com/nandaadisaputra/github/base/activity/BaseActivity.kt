package com.nandaadisaputra.github.base.activity

import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.google.gson.Gson
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.datastore.DataStorePreference
import javax.inject.Inject

open class BaseActivity<VB: ViewDataBinding, VM: CoreViewModel>(layoutRes: Int): CoreActivity<VB, VM>(layoutRes){
    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var appDatabase: UserDatabase

    @Inject
    lateinit var session: CoreSession

    @Inject
    lateinit var dataStore: DataStorePreference

}