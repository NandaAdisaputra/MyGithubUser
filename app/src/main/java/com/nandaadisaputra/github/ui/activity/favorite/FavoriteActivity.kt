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

@AndroidEntryPoint
class FavoriteActivity :
    BaseActivity<ActivityFavoriteBinding, FavoriteViewModel>(R.layout.activity_favorite) {
    private val favoriteUser = ArrayList<UsersEntity?>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this
        swipeRefresh()
        initAppbar()
        observeApp()
        darkMode()
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

    private fun swipeRefresh() {
        binding.swiftLayout.setOnRefreshListener {
            swipeRefresh()
            initAppbar()
            observeApp()
            binding.swiftLayout.isRefreshing = false
        }
    }

    private fun observeApp() {
        showLoading(true)
        viewModel.getAllFavorites()?.observe(this) {
            binding.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user
            )
                .initItem(favoriteUser) { position, data ->
                    openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
        }
        fun setList(user: ArrayList<UsersEntity>) {
            favoriteUser.clear()
            favoriteUser.addAll(user)
            binding.rvFavoriteUser.adapter?.notifyDataSetChanged()
            showLoading(false)
        }

        viewModel.getAllFavorites()?.observe(this) { favoriteList ->
            if (favoriteList != null) {
                if (favoriteList.isNotEmpty()) {
                    val user = mapList(favoriteList)
                    setList(user)
                    showEmpty(false)
                } else {
                    showEmpty(true)
                    showLoading(false)
                }
            }
        }
    }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_setting -> {
                openActivity<SettingsActivity> { }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.Cons.FAVORITE_USER
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun showEmpty(state: Boolean) {
        binding.favoriteEState.root.isVisible = state
        binding.rvFavoriteUser.isGone = state
    }
    private fun showLoading(state: Boolean) {
        binding.progressbar.isVisible = state
    }
}
