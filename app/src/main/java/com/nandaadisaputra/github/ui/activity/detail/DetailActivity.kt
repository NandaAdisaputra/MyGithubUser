package com.nandaadisaputra.github.ui.activity.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.adapter.ViewPagerAdapter
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.data.constant.Const
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this
        observe()
        initPageAdapter()
        initAppbar()
        darkMode()
        favorite()
    }
    private fun observe() {
        viewModel.getUserDetail().observe(this) {
            Const.Cons.NULL
            if (it != null) {
                binding.apply {
                    tvDetailLogin.text = it.login
                    tvDetailName.text = it.name
                    tvDetailBio.text = it.bio
                    tvFollower.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvRepository.text = it.public_repos.toString()
                    tvDetailLocation.text = it.location
                    ForGlide.loadImage( ivDetailImage,it.image,)
                }
            }else{
                showEmpty(true)
            }

        }
    }
    private fun favorite(){
        val id = intent.getIntExtra(Const.User.D_ID, 0)
        val avatar = intent.getStringExtra(Const.User.D_AVATAR)
        var isFavorite = false
        val username = intent.getStringExtra(Const.User.D_USERNAME)
        username?.let { viewModel.setUserDetail(it) }
        if (username != null) {
            viewModel.setUserDetail(username)
        }
        val bundle = Bundle()
        bundle.putString(Const.User.D_USERNAME, username)
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    setStatusFavorite(true)
                    isFavorite = true
                } else {
                    setStatusFavorite(false)
                }
            }
        }
        binding.fabFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                if (username != null) {
                    viewModel.addToFavorite(username, id, avatar)
                }
                loadingDialog.setResponse(Const.Likes.LOADING_LIKE, R.drawable.ic_favorite)
            } else {
                loadingDialog.setResponse(Const.Likes.LOADING_UNLIKE, R.drawable.ic_favorite_border)
                viewModel.removeFromFavorite(id)
            }
            setStatusFavorite(isFavorite)
        }
    }
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

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

    override fun onDestroy() {
        super.onDestroy()
        observe()
    }

    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.Cons.DETAIL_USER
    }

    private fun initPageAdapter() {
        val sectionsPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.detail_tab)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_favorite -> {
                openActivity<FavoriteActivity> {
                }
                true
            }
            R.id.icon_setting -> {
                openActivity<SettingsActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setIconResource(R.drawable.ic_favorite)
            binding.fabFavorite.text = Const.Likes.LIKE
        } else {
            binding.fabFavorite.setIconResource(R.drawable.ic_favorite_border)
            binding.fabFavorite.text = Const.Likes.UN_LIKE
        }
    }
    private fun showEmpty(state: Boolean) {
        binding.vEmpty.isVisible = state
        binding.detail.isGone = state
    }
}