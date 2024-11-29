package com.nandaadisaputra.github.ui.activity.settings


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.databinding.ActivitySettingsBinding
import com.nandaadisaputra.github.ui.activity.favorite.FavoriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity :
    BaseActivity<ActivitySettingsBinding, SettingViewModel>(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppbar()  // Menginisialisasi AppBar (Toolbar)
        setupListener()  // Menyiapkan listener untuk interaksi pengguna
        observer()  // Menyiapkan pengamat untuk LiveData (tema)
        setSupportActionBar(binding.toolbarSetting)  // Menetapkan toolbar sebagai ActionBar
        binding.btnLanguage.setOnClickListener {
            // Menangani klik pada tombol pengaturan bahasa, membuka pengaturan bahasa sistem
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

    // Fungsi untuk mengamati perubahan tema dari ViewModel
    private fun observer() {
        viewModel.getTheme.observe(this@SettingsActivity) { isDarkMode ->
            checkDarkMode(isDarkMode)  // Mengubah mode tampilan berdasarkan status tema
        }
    }

    // Fungsi untuk mengatur listener pada switch tema
    private fun setupListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Memanggil ViewModel untuk mengubah status tema sesuai pilihan pengguna
                    when (isChecked) {
                        true -> viewModel.setTheme(true)  // Tema gelap
                        false -> viewModel.setTheme(false)  // Tema terang
                    }
                }
            }
        }
    }

    // Fungsi untuk memeriksa dan mengubah mode tampilan (gelap/terang)
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

    // Fungsi untuk menampilkan menu pengaturan pada toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)  // Memuat menu XML
        return super.onCreateOptionsMenu(menu)
    }

    // Fungsi untuk menangani item menu yang dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Jika item pengaturan dipilih, membuka SettingsActivity
        if (item.itemId == R.id.icon_setting) {
            openActivity<SettingsActivity> { }
        }
        // Jika item favorit dipilih, membuka FavoriteActivity
        if (item.itemId == R.id.icon_favorite) {
            openActivity<FavoriteActivity> { }
        }
        return super.onOptionsItemSelected(item)
    }

    // Fungsi untuk menangani tombol navigasi kembali di toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()  // Menangani klik tombol kembali
        return true
    }

    // Fungsi untuk menginisialisasi AppBar dengan judul dan tombol kembali
    private fun initAppbar() {
        val actionBar = supportActionBar
        actionBar?.title = Const.Cons.DETAIL_USER  // Menetapkan judul AppBar
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Menampilkan tombol kembali di AppBar
    }
}