package com.nandaadisaputra.github.base.activity

import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.google.gson.Gson
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.datastore.DataStorePreference
import javax.inject.Inject

open class BaseActivity<VB: ViewDataBinding, VM: CoreViewModel>(layoutRes: Int): CoreActivity<VB, VM>(layoutRes) {
    // Kelas ini adalah kelas dasar yang dapat digunakan untuk aktivitas lain dengan ViewDataBinding dan ViewModel.

    @Inject
    lateinit var gson: Gson // Menyuntikkan dependensi Gson untuk serialisasi/deserialisasi JSON.

    @Inject
    lateinit var appDatabase: UserDatabase // Menyuntikkan dependensi UserDatabase untuk operasi database lokal.

    @Inject
    lateinit var session: CoreSession // Menyuntikkan dependensi CoreSession untuk manajemen sesi pengguna.

    @Inject
    lateinit var dataStore: DataStorePreference // Menyuntikkan dependensi DataStorePreference untuk penyimpanan data preferensi pengguna.
}
