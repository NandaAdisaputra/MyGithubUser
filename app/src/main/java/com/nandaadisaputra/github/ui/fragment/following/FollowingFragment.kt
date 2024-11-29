package com.nandaadisaputra.github.ui.fragment.following

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
import com.nandaadisaputra.github.databinding.FragmentFollowingBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Menandakan bahwa fragment ini menggunakan Hilt untuk Dependency Injection.
class FollowingFragment : BaseFragment<FragmentFollowingBinding>(R.layout.fragment_following) {

    // Menyimpan daftar pengguna yang di-follow
    private val followingUser = ArrayList<UsersEntity?>()

    // Mendeklarasikan ViewModel untuk mengelola data
    private lateinit var viewModel: FollowingViewModel

    // Fungsi ini dipanggil setelah fragment selesai dibuat dan tampilan fragment sudah siap.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menginisialisasi ViewModel dan menghubungkannya dengan binding agar bisa mengamati LiveData
        viewModel = ViewModelProvider(this@FollowingFragment)[FollowingViewModel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner // Mengatur lifecycle owner agar LiveData dapat diamati dengan benar
        binding?.viewModel = viewModel // Menghubungkan ViewModel dengan layout untuk binding data ke tampilan

        // Mengambil data pengguna yang diteruskan dari activity sebelumnya melalui Intent
        val extraUser = activity?.intent?.getParcelableExtra<UsersEntity>(DetailActivity.EXTRA_USER) as UsersEntity

        // Jika data pengguna ada, ambil username-nya dan siapkan ViewModel untuk mengambil daftar following
        extraUser.login?.let { setupViewModel(it) }

        // Menyiapkan RecyclerView dan adapter
        setupRecyclerView()
        initAdapter()
    }

    // Fungsi untuk menyiapkan adapter RecyclerView dan menangani interaksi item
    private fun initAdapter() {
        // Mengamati data 'following' dari ViewModel untuk diperbarui pada RecyclerView
        viewModel.following.observe(viewLifecycleOwner) {
            // Menyiapkan adapter dengan layout item_user untuk setiap item dalam daftar following
            binding?.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(R.layout.item_user)
                .initItem(followingUser) { _, data ->
                    // Menangani klik item untuk membuka DetailActivity dan menampilkan data pengguna
                    context?.openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
        }
    }

    // Fungsi untuk menyiapkan RecyclerView dengan LinearLayoutManager untuk daftar vertikal
    private fun setupRecyclerView() {
        binding?.apply {
            rvFollowing.layoutManager = LinearLayoutManager(activity) // Mengatur layout manager agar vertikal
            rvFollowing.setHasFixedSize(true) // Menetapkan ukuran tetap untuk RecyclerView
        }
    }

    // Fungsi untuk mengatur ViewModel dengan username dan memperbarui daftar pengguna yang di-follow
    private fun setupViewModel(username: String) {
        viewModel.apply {
            setListFollowing(username) // Meminta daftar following berdasarkan username yang diterima

            // Fungsi untuk memperbarui daftar following dan memberitahu adapter agar tampilan di-refresh
            fun setList(user: ArrayList<UsersEntity>) {
                followingUser.clear() // Menghapus data lama dalam daftar
                followingUser.addAll(user) // Menambahkan data baru yang diperoleh
                binding?.rvFollowing?.adapter?.notifyDataSetChanged() // Memberi tahu adapter untuk memperbarui tampilan
            }

            // Mengamati perubahan pada daftar following
            getListFollowing().observe(viewLifecycleOwner) {
                if (it != null) {
                    setList(it) // Memperbarui daftar following jika data ada
                }
                // Menampilkan tampilan kosong jika daftar following kosong
                if (it.isEmpty()) {
                    showEmpty(true)
                }
            }
        }
    }

    // Fungsi untuk membersihkan binding saat fragment dihancurkan agar menghindari memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Menghapus binding agar tidak ada referensi yang tertinggal
    }

    // Fungsi untuk menampilkan atau menyembunyikan tampilan kosong jika tidak ada data yang ditampilkan
    private fun showEmpty(state: Boolean) {
        binding?.vEmpty?.isVisible = state // Menampilkan tampilan kosong jika state true
        binding?.rvFollowing?.isGone = state // Menyembunyikan RecyclerView jika state true
    }
}
