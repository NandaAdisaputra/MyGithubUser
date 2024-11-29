package com.nandaadisaputra.github.ui.activity.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.crocodic.core.extension.openActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.adapter.ViewPagerAdapter
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.data.room.user.detail.DetailUserEntity
import com.nandaadisaputra.github.databinding.ActivityDetailBinding
import com.nandaadisaputra.github.ui.activity.favorite.FavoriteActivity
import com.nandaadisaputra.github.ui.activity.settings.SettingsActivity
import com.nandaadisaputra.github.utils.ForGlide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {

    // Konstanta untuk extra data yang diterima oleh Activity
    companion object {
        const val EXTRA_USER = "extra_user"

        // Daftar judul untuk tab
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding untuk ViewModel dan Activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        // Inisialisasi adapter untuk view pager dan setup appbar
        initPageAdapter()
        initAppbar()

        // Mengatur tema gelap atau terang sesuai preferensi pengguna
        darkMode()

        // Mendapatkan data user dari intent
        val user = intent.getParcelableExtra<UsersEntity>(EXTRA_USER) as UsersEntity
        user.login?.let { setupViewModel(it) }

        // Mengatur status favorit berdasarkan data user
        val avatar = user.avatar.toString()
        val username = user.login.toString()
        var isFavorite = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = user.id?.let { viewModel.checkUser(it) }
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        setStatusFavorite(true)
                        isFavorite = true
                    } else {
                        setStatusFavorite(false)
                    }
                }
            }
        }

        // Menangani aksi klik pada tombol favorit
        binding.fabFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                user.id?.let { it1 -> viewModel.addToFavorite(username, it1, avatar) }
                loadingDialog.setResponse(Const.Likes.LOADING_LIKE, R.drawable.ic_favorite)
            } else {
                loadingDialog.setResponse(Const.Likes.LOADING_UNLIKE, R.drawable.ic_favorite_border)
                user.id?.let { it1 -> viewModel.removeFromFavorite(it1) }
            }
            setStatusFavorite(isFavorite)
        }
    }

    // Setup ViewModel dengan username dari user
    private fun setupViewModel(username: String) {
        viewModel.setUserDetail(username)
        viewModel.user.observe(this) {
            userDetail(it)
        }
    }

    // Menampilkan detail user pada UI
    private fun userDetail(detailUserEntity: DetailUserEntity) {
        viewModel.getUserDetail().observe(this) {
            Const.Cons.NULL
            binding.apply {
                tvDetailLogin.text = detailUserEntity.login
                tvDetailName.text = detailUserEntity.name
                tvDetailBio.text = detailUserEntity.bio
                tvFollower.text = detailUserEntity.followers.toString()
                tvFollowing.text = detailUserEntity.following.toString()
                tvRepository.text = detailUserEntity.public_repos.toString()
                tvDetailLocation.text = detailUserEntity.location
                // Memuat gambar profil menggunakan Glide
                ForGlide.loadImage(ivDetailImage, detailUserEntity.image)
            }
        }
    }

    // Mengatur tema gelap berdasarkan preferensi pengguna
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

    // Memeriksa dan mengatur mode gelap atau terang
    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    // Menghentikan ViewModel ketika Activity dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        setupViewModel(username = String())
    }

    // Inisialisasi AppBar untuk Activity Detail
    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.Cons.DETAIL_USER
    }

    // Menginisialisasi ViewPager dan TabLayout
    private fun initPageAdapter() {
        val sectionsPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.detail_tab)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    // Membuat menu pengaturan pada ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    // Menangani aksi item menu pengaturan
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_favorite -> {
                openActivity<FavoriteActivity> {}
                true
            }
            R.id.icon_setting -> {
                openActivity<SettingsActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Mengubah status favorit pada UI
    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setIconResource(R.drawable.ic_favorite)
            binding.fabFavorite.text = Const.Likes.LIKE
        } else {
            binding.fabFavorite.setIconResource(R.drawable.ic_favorite_border)
            binding.fabFavorite.text = Const.Likes.UN_LIKE
        }
    }

}
