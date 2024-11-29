package com.nandaadisaputra.github.ui.activity.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.databinding.ActivitySplashScreenBinding
import com.nandaadisaputra.github.ui.activity.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreenActivity: BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel>(R.layout.activity_splash_screen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur mode gelap berdasarkan tema yang disimpan
        darkMode()
        // Menunggu selama 3 detik dan langsung buka HomeActivity
        viewModel.splash {
            openActivity<HomeActivity>()  // Langsung menuju HomeActivity
//            if (it) {
//                openActivity<HomeActivity>()
//            } else {
//                openActivity<LoginActivity>()
//            }
            finish() // Menutup SplashScreenActivity
        }
    }
    // Fungsi untuk mengamati tema dan mengatur mode gelap
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }
    // Fungsi untuk mengatur mode gelap
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
}