package com.nandaadisaputra.github.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nandaadisaputra.github.ui.fragment.followers.FollowersFragment
import com.nandaadisaputra.github.ui.fragment.following.FollowingFragment

class ViewPagerAdapter(activity: DetailActivity) : FragmentStateAdapter(activity) { // Mengatur adapter untuk ViewPager2 dengan 2 fragment

    override fun getItemCount(): Int = 2 // Menentukan jumlah fragment yang akan ditampilkan, dalam hal ini ada 2 fragment

    override fun createFragment(position: Int): Fragment { // Membuat fragment berdasarkan posisi yang diberikan
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment() // Jika posisi 0, tampilkan fragment followers
            1 -> fragment = FollowingFragment() // Jika posisi 1, tampilkan fragment following
        }
        return fragment as Fragment // Mengembalikan fragment yang dipilih
    }
}