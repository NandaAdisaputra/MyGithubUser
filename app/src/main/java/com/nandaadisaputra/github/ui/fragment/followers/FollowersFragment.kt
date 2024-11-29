package com.nandaadisaputra.github.ui.fragment.followers

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.fragment.BaseFragment
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.databinding.FragmentFollowersBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>(R.layout.fragment_followers) {

    private val follower = ArrayList<UsersEntity?>() // Menyimpan daftar followers
    private lateinit var viewModel: FollowersViewModel // Menyimpan referensi ke FollowersViewModel

    // Fungsi ini dipanggil setelah fragment selesai dibuat dan tampilan fragment sudah siap.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menginisialisasi ViewModel untuk fragment ini menggunakan ViewModelProvider
        viewModel = ViewModelProvider(this@FollowersFragment)[FollowersViewModel::class.java]

        // Menghubungkan ViewModel dengan layout binding agar data bisa dibinding ke tampilan
        binding?.lifecycleOwner = viewLifecycleOwner // Mengatur lifecycle owner agar LiveData dapat diamati
        binding?.viewModel = viewModel // Menghubungkan ViewModel dengan layout

        // Mendapatkan data pengguna yang dikirim melalui Intent dari activity sebelumnya
        val extraUser =
            activity?.intent?.getParcelableExtra<UsersEntity>(DetailActivity.EXTRA_USER) as UsersEntity

        // Jika data pengguna ada, ambil username-nya dan siapkan ViewModel untuk mengambil daftar followers
        extraUser.login?.let { setupViewModel(it) }

        // Menyiapkan RecyclerView untuk menampilkan daftar followers
        setupRecyclerView()

        // Menginisialisasi adapter untuk RecyclerView
        initAdapter()
    }

    // Fungsi untuk menginisialisasi adapter dan mengamati perubahan data followers
    private fun initAdapter() {
        // Mengamati perubahan data 'followers' dari ViewModel
        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            // Mengatur adapter untuk RecyclerView dan menghubungkannya dengan data followers
            binding?.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(R.layout.item_user)
                .initItem(follower) { _, data ->
                    // Ketika item di RecyclerView diklik, buka DetailActivity untuk melihat detail pengguna
                    context?.openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
        }
    }

    // Fungsi untuk menyiapkan RecyclerView agar menggunakan LinearLayoutManager
    private fun setupRecyclerView() {
        binding?.apply {
            rvFollower.layoutManager = LinearLayoutManager(activity) // Menentukan layout untuk RecyclerView agar daftar vertikal
            rvFollower.setHasFixedSize(true) // Menentukan RecyclerView tidak akan berubah ukurannya meskipun data berubah
        }
    }

    // Fungsi untuk menyiapkan ViewModel berdasarkan username pengguna
    private fun setupViewModel(username: String) {
        viewModel.apply {
            setListFollowers(username) // Mengambil daftar followers dari API berdasarkan username

            // Fungsi untuk memperbarui daftar followers pada UI
            fun setList(user: ArrayList<UsersEntity>) {
                follower.clear() // Membersihkan daftar followers yang lama
                follower.addAll(user) // Menambahkan daftar followers yang baru
                binding?.rvFollower?.adapter?.notifyDataSetChanged() // Memberi tahu adapter untuk memperbarui data
            }

            // Mengamati perubahan data followers dan memperbarui UI
            getListFollowers().observe(viewLifecycleOwner) {
                if (it != null) {
                    setList(it) // Memperbarui daftar followers jika data ada
                }
                // Menampilkan tampilan kosong jika daftar followers kosong
                if (it.isEmpty()) {
                    showEmpty(true) // Menampilkan tampilan kosong
                }
            }
        }
    }

    // Fungsi untuk membersihkan binding saat fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Membebaskan referensi binding agar tidak ada memory leak
    }

    // Fungsi untuk menampilkan atau menyembunyikan tampilan kosong
    private fun showEmpty(state: Boolean) {
        binding?.vEmpty?.isVisible = state // Menampilkan tampilan kosong jika state true
        binding?.rvFollower?.isGone = state // Menyembunyikan RecyclerView jika tampilan kosong ditampilkan
    }
}
