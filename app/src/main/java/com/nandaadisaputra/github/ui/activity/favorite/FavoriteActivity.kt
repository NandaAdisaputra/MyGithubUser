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
import com.nandaadisaputra.github.ui.activity.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity :
    BaseActivity<ActivityFavoriteBinding, FavoriteViewModel>(R.layout.activity_favorite) {

    private val favoriteUser = ArrayList<UsersEntity>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this
        initUI()
    }

    // Inisialisasi UI dan mengamati data yang diperlukan
    private fun initUI() {
        initAppbar()
        swipeRefresh()
        observeTheme()
        observeFavorites()
    }

    // Mengamati perubahan tema (mode gelap/terang)
    private fun observeTheme() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            setDarkMode(isDarkMode)
        }
    }

    // Menyesuaikan mode gelap atau terang
    private fun setDarkMode(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    // Mengatur swipe-to-refresh untuk memperbarui data
    private fun swipeRefresh() {
        binding.swiftLayout.setOnRefreshListener {
            observeFavorites() // Memanggil ulang pengamatan daftar favorit
            binding.swiftLayout.isRefreshing = false
        }
    }

    // Mengamati data favorit dan memperbarui UI jika data berubah
    private fun observeFavorites() {
        showLoading(true)  // Menampilkan loading
        viewModel.getAllFavorites()?.observe(this) { favoriteList ->
            if (favoriteList.isNullOrEmpty()) {
                showEmpty(true) // Menampilkan pesan kosong jika tidak ada data
                showLoading(false)
            } else {
                val users = mapList(favoriteList)
                updateFavoriteList(users) // Memperbarui daftar favorit
                showEmpty(false) // Menyembunyikan pesan kosong
            }
        }
    }

    // Mengonversi FavoriteEntity menjadi UsersEntity untuk ditampilkan
    private fun mapList(listFavorites: List<FavoriteEntity>): ArrayList<UsersEntity> {
        val userList = ArrayList<UsersEntity>()
        for (user in listFavorites) {
            user.username?.let { username ->
                user.avatarUrl?.let { avatarUrl ->
                    userList.add(UsersEntity(login = username, id = user.id, avatar = avatarUrl))
                }
            }
        }
        return userList
    }

    // Memperbarui daftar favorit pada RecyclerView dengan efisien
    private fun updateFavoriteList(users: ArrayList<UsersEntity>) {
        val previousSize = favoriteUser.size

        // Memperbarui data pada daftar favorit
        favoriteUser.clear()
        favoriteUser.addAll(users)

        // Menggunakan notifikasi perubahan data yang lebih efisien
        if (previousSize == 0) {
            // Jika sebelumnya daftar kosong, maka tambahkan item baru
            binding.rvFavoriteUser.adapter?.notifyItemRangeInserted(0, favoriteUser.size)
        } else {
            // Jika ada perubahan data namun tidak mereset semua item
            binding.rvFavoriteUser.adapter?.notifyItemRangeChanged(0, favoriteUser.size)
        }

        showLoading(false) // Menyembunyikan loading setelah data berhasil diperbarui
    }

    // Menampilkan atau menyembunyikan tampilan kosong jika tidak ada data
    private fun showEmpty(isEmpty: Boolean) {
        binding.favoriteEState.root.isVisible = isEmpty
        binding.rvFavoriteUser.isGone = isEmpty
    }

    // Menampilkan atau menyembunyikan progress bar saat loading
    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.isVisible = isLoading
    }

    // Menyiapkan menu pengaturan pada toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    // Menangani pemilihan item pada menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_setting -> {
                openActivity<SettingsActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Menyiapkan app bar dengan judul dan tombol back
    private fun initAppbar() {
        supportActionBar?.apply {
            title = Const.Cons.FAVORITE_USER
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    // Menangani tombol back pada app bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()  //untuk menangani tombol kembali
        return true
    }
}
