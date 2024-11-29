package com.nandaadisaputra.github.ui.activity.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.favorite.FavoriteEntity
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.databinding.ActivityFavoriteBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nandaadisaputra.github.ui.activity.settings.SettingsActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

// Menandakan bahwa kelas ini menggunakan Hilt untuk Dependency Injection
@AndroidEntryPoint
class FavoriteActivity :
    BaseActivity<ActivityFavoriteBinding, FavoriteViewModel>(R.layout.activity_favorite) {

    // Menyimpan daftar pengguna yang difavoritkan
    private val favoriteUser = ArrayList<UsersEntity?>()

    // Fungsi onCreate dijalankan ketika activity pertama kali dibuat
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan binding, viewModel, dan activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        // Menginisialisasi swipe refresh, appbar, dan observer data
        swipeRefresh()
        initAppbar()
        observeApp()

        // Menangani mode gelap
        darkMode()
    }

    // Fungsi untuk mengatur mode gelap berdasarkan nilai dari viewModel
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

    // Fungsi untuk mengecek dan mengatur mode gelap atau terang
    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                // Mengaktifkan mode gelap
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                // Mengaktifkan mode terang
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    // Fungsi untuk mengatur swipe refresh
    private fun swipeRefresh() {
        binding.swiftLayout.setOnRefreshListener {
            // Mengulang proses refresh ketika swipe
            swipeRefresh()
            initAppbar()
            observeApp()
            binding.swiftLayout.isRefreshing = false
        }
    }

    // Fungsi untuk mengamati data dan memperbarui tampilan sesuai data yang diterima
    private fun observeApp() {
        showLoading(true)
        // Mengambil daftar favorit dari viewModel dan mengupdate tampilan
        viewModel.getAllFavorites()?.observe(this) {
            binding.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user
            )
                .initItem(favoriteUser) { position, data ->
                    // Membuka activity detail pengguna saat item diklik
                    openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
        }

        // Fungsi untuk menampilkan daftar pengguna setelah data diterima
        fun setList(user: ArrayList<UsersEntity>) {
            favoriteUser.clear()
            favoriteUser.addAll(user)
            binding.rvFavoriteUser.adapter?.notifyDataSetChanged()
            showLoading(false)
        }

        // Mengamati perubahan pada data favorit
        viewModel.getAllFavorites()?.observe(this) { favoriteList ->
            if (favoriteList != null) {
                // Jika ada data favorit, map data dan tampilkan
                if (favoriteList.isNotEmpty()) {
                    val user = mapList(favoriteList)
                    setList(user)
                    showEmpty(false)
                } else {
                    // Jika tidak ada data favorit, tampilkan status kosong
                    showEmpty(true)
                    showLoading(false)
                }
            }
        }
    }

    // Fungsi untuk memetakan data FavoriteEntity menjadi UsersEntity
    private fun mapList(listFavorites: List<FavoriteEntity>): ArrayList<UsersEntity> {
        val listUser = ArrayList<UsersEntity>()
        for (user in listFavorites) {
            val userMapped = user.username?.let {
                user.avatarUrl?.let { it1 ->
                    UsersEntity(
                        login = it,
                        id = user.id,
                        avatar = it1,
                    )
                }
            }
            userMapped?.let { listUser.add(it) }
        }
        return listUser
    }

    // Menampilkan menu pengaturan di action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    // Mengatur aksi ketika item menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_setting -> {
                // Membuka activity pengaturan
                openActivity<SettingsActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Menginisialisasi appbar (action bar) dengan judul dan ikon kembali
    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.Cons.FAVORITE_USER
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Mengatur aksi ketika tombol kembali di tekan
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Fungsi untuk menampilkan atau menyembunyikan tampilan kosong
    private fun showEmpty(state: Boolean) {
        binding.favoriteEState.root.isVisible = state
        binding.rvFavoriteUser.isGone = state
    }

    // Fungsi untuk menampilkan atau menyembunyikan loading indicator
    private fun showLoading(state: Boolean) {
        binding.progressbar.isVisible = state
    }
}
