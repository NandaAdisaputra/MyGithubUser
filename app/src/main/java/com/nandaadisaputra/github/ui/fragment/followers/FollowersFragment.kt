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
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.databinding.FragmentFollowersBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>(R.layout.fragment_followers) {
    private val follower = ArrayList<UsersEntity?>()
    private lateinit var viewModel: FollowersViewModel
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this@FollowersFragment)[FollowersViewModel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = viewModel

        val arg = arguments
        username = arg?.getString(Const.User.D_USERNAME).toString()
        viewModel.listFollowers.observe(viewLifecycleOwner) {
            binding?.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user)
                .initItem(follower) { position, data ->
                    context?.openActivity<DetailActivity> {
                        putExtra(Const.User.D_USERNAME, data?.login)
                        putExtra(Const.User.D_ID, data?.id)
                        putExtra(Const.User.D_AVATAR, data?.avatar)
                    }
                }
            viewModel.getListFollowers()
        }
        binding?.apply {
            rvFollower.layoutManager = LinearLayoutManager(activity)
            rvFollower.setHasFixedSize(true)
        }
        viewModel.apply {
            setListFollowers(username)
            fun setList(user: ArrayList<UsersEntity>) {
                follower.clear()
                follower.addAll(user)
                binding?.rvFollower?.adapter?.notifyDataSetChanged()
            }
            getListFollowers().observe(viewLifecycleOwner) {
                if (it != null) {
                    setList(it)
                }else{
                    showEmpty(true)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    private fun showEmpty(state: Boolean) {
        binding?.vEmpty?.isVisible = state
        binding?.rvFollower?.isGone = state
    }

}