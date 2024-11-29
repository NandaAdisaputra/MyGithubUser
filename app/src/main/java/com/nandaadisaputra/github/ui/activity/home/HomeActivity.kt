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

    // Daftar users untuk ditampilkan di RecyclerView
    private val users = ArrayList<UsersEntity?>()

    // Daftar gambar untuk slider
    private val imageSlider = listOf(
        "https://i.kym-cdn.com/photos/images/original/001/704/393/8d2.png",
        "https://qph.cf2.quoracdn.net/main-qimg-729a22aba98d1235fdce4883accaf81e"
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengatur binding dan ViewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        // Memanggil fungsi-fungsi awal
        searchUser()
        getSlider()
        darkMode()

        // Observasi data pengguna dari ViewModel
        viewModel.listUsers.observe(this) {
            // Menginisialisasi adapter dengan data pengguna
            binding.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user
            ).initItem(users) { position, data ->
                // Aksi ketika item diklik, membuka DetailActivity
                openActivity<DetailActivity> {
                    putExtra(DetailActivity.EXTRA_USER, data)
                }
            }
            // Memanggil metode untuk mendapatkan data pengguna
            viewModel.getSearchUsers()
        }

        // Menambahkan listener pada search bar untuk mendeteksi tombol enter
        binding.apply {
            searchUser.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser() // Menjalankan pencarian
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        // Fungsi untuk memperbarui daftar pengguna
        fun setList(user: ArrayList<UsersEntity>) {
            users.clear()
            users.addAll(user)
            binding.rvUsers.adapter?.notifyDataSetChanged()
        }

        // Mengamati perubahan data pengguna dari pencarian
        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                setList(it) // Memperbarui daftar jika ada data
            } else {
                showEmpty(true) // Menampilkan tampilan kosong jika tidak ada data
            }
        }
    }

    // Fungsi untuk pengaturan pencarian pengguna
    private fun searchUser() {
        binding.apply {
            searchUser.queryHint = Const.Cons.SEARCH // Menetapkan hint pada search bar
            searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            viewModel?.setSearchUsers(query) // Menyaring pengguna berdasarkan query
                        } else {
                            showEmpty(true) // Menampilkan tampilan kosong jika query kosong
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })
            searchUser.setOnCloseListener {
                searchUser.setQuery(Const.Cons.EMPTY, false) // Mengosongkan query saat menutup pencarian
                loadingDialog.dismiss() // Menutup loading dialog
                true
            }
        }
    }

    // Fungsi untuk mengatur tema gelap atau terang
    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode) // Mengecek dan mengubah mode berdasarkan preferensi
        }
    }

    // Fungsi untuk mengubah mode tampilan (gelap atau terang)
    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Mode gelap
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Mode terang
            }
        }
    }

    // Fungsi untuk menampilkan slider gambar
    private fun getSlider() {
        for (item in 0 until imageSlider.count()) {
            val textSliderView = TextSliderView(this).apply {
                description("Welcome in My Github App") // Deskripsi gambar slider
                image(imageSlider[item]) // Menambahkan gambar
                setOnSliderClickListener(this@HomeActivity) // Menambahkan listener
                scaleType = BaseSliderView.ScaleType.FitCenterCrop // Menentukan cara penyesuaian gambar
            }
            binding.slider.addSlider(textSliderView) // Menambahkan slider ke tampilan
        }
    }

    // Menghentikan auto cycle slider saat activity berhenti
    override fun onStop() {
        binding.slider.stopAutoCycle()
        super.onStop()
    }

    // Listener untuk klik pada slider
    override fun onSliderClick(slider: BaseSliderView?) {
    }

    // Listener untuk perubahan halaman slider
    override fun onPageScrollStateChanged(state: Int) {
    }

    // Listener untuk pergeseran halaman slider
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    // Listener untuk pemilihan halaman slider
    override fun onPageSelected(position: Int) {
    }

    // Membuat menu pada action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu) // Menambahkan menu dari resource
        return super.onCreateOptionsMenu(menu)
    }

    // Menangani pemilihan item menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_favorite -> openActivity<FavoriteActivity>() // Menampilkan FavoriteActivity
            R.id.icon_setting -> openActivity<SettingsActivity>() // Menampilkan SettingsActivity
        }
        return super.onOptionsItemSelected(item)
    }

    // Fungsi untuk menampilkan atau menyembunyikan tampilan kosong
    private fun showEmpty(state: Boolean) {
        binding.vEmpty.isVisible = state // Menampilkan tampilan kosong
        binding.rvUsers.isGone = state // Menyembunyikan RecyclerView
    }
}
