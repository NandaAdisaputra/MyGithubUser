package com.nandaadisaputra.github.ui.activity.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.crocodic.core.extension.openActivity
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.databinding.ActivityHomeBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nandaadisaputra.github.ui.activity.favorite.FavoriteActivity
import com.nandaadisaputra.github.ui.activity.settings.SettingsActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home),
    BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private val users = ArrayList<UsersEntity?>()
    private val imageSlider = listOf(
        "https://i.kym-cdn.com/photos/images/original/001/704/393/8d2.png",
        "https://qph.cf2.quoracdn.net/main-qimg-729a22aba98d1235fdce4883accaf81e"
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this
        searchUser()
        getSlider()
        darkMode()

        viewModel.listUsers.observe(this) {
            binding.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user)
                .initItem(users) { position, data ->
                    openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
            viewModel.getSearchUsers()
        }
        binding.apply {
            searchUser.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
        fun setList(user: ArrayList<UsersEntity>) {
            users.clear()
            users.addAll(user)
            binding.rvUsers.adapter?.notifyDataSetChanged()
        }
        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                setList(it)
            } else{
                showEmpty(true)
            }
        }
    }

    private fun searchUser() {
        binding.apply {
            searchUser.queryHint = Const.Cons.SEARCH
            searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            viewModel?.setSearchUsers(query)
                        } else{
                            showEmpty(true)
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })
            searchUser.setOnCloseListener {
                searchUser.setQuery(Const.Cons.EMPTY, false)
                loadingDialog.dismiss()
                true
            }

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

    private fun getSlider() {
        for (item in 0 until imageSlider.count()) {
            val textSliderView = TextSliderView(this).apply {
                description("Welcome in My Github App")
                image(imageSlider[item])
                setOnSliderClickListener(this@HomeActivity)
                scaleType = BaseSliderView.ScaleType.FitCenterCrop
            }
            binding.slider.addSlider(textSliderView)

        }
    }

    override fun onStop() {
        binding.slider.stopAutoCycle()
        super.onStop()
    }

    override fun onSliderClick(slider: BaseSliderView?) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_favorite -> openActivity<FavoriteActivity>()
            R.id.icon_setting -> openActivity<SettingsActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showEmpty(state: Boolean) {
        binding.vEmpty.isVisible = state
        binding.rvUsers.isGone = state
    }
}