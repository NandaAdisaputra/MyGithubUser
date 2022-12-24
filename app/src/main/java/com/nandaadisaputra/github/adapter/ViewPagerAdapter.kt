package com.nandaadisaputra.github.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nandaadisaputra.github.ui.fragment.followers.FollowersFragment
import com.nandaadisaputra.github.ui.fragment.following.FollowingFragment

class ViewPagerAdapter(activity: DetailActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }
}